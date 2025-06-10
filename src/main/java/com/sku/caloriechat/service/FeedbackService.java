package com.sku.caloriechat.service;

import com.sku.caloriechat.dao.FeedbackDao;
import com.sku.caloriechat.dao.FoodItemDao;
import com.sku.caloriechat.dao.MealDao;
import com.sku.caloriechat.domain.FeedbackLog;
import com.sku.caloriechat.domain.FoodItem;
import com.sku.caloriechat.domain.Meal;
import com.sku.caloriechat.dto.FoodItemSaveDto;
import com.sku.caloriechat.dto.MealFeedbackRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackDao feedbackDao;
    private final GptService gptService;
    private final MealDao mealDao;
    private final FoodItemDao foodItemDao;

    /** 피드백 저장 */
    public void saveFeedback(Long userId, String feedbackText) {
        FeedbackLog log = new FeedbackLog();
        log.setUserId(userId);
        log.setDate(LocalDate.now());
        log.setFeedback(feedbackText);
        log.setCreatedAt(LocalDateTime.now());

        feedbackDao.save(log);
    }

    /** GPT로 피드백 생성 후 저장 (FoodItem 기반 수동 요청) */
    public void generateAndSaveFeedback(Long userId, MealFeedbackRequestDto requestDto) {
        validateFeedbackRequestInterval(userId);  // 🔒 요청 간격 제한 체크

        String prompt = buildPrompt(requestDto.getEatenAt(), requestDto.getFoodItems());
        String feedback = gptService.getFeedback(prompt);
        saveFeedback(userId, feedback);
    }

    //** 저장된 mealId 기반 GPT 피드백 생성 */
    public String generateFeedbackFromMeal(Long userId, Long mealId) {
        validateFeedbackRequestInterval(userId);  // 🔒 시간 제한

        try {
            Meal meal = mealDao.findById(mealId.intValue());

            // ✅ eatenAt 날짜가 오늘이 아닐 경우 예외 발생
            if (!meal.getEatenAt().toLocalDate().equals(LocalDate.now())) {
                throw new IllegalStateException("오늘 섭취한 식단에 대해서만 피드백을 요청할 수 있습니다.");
            }

            List<FoodItem> foodItems = foodItemDao.findByMealId(mealId.intValue());

            List<FoodItemSaveDto> dtoList = foodItems.stream()
                    .map(f -> new FoodItemSaveDto(f.getName(), f.getCalories(), f.getQuantity()))
                    .collect(Collectors.toList());

            MealFeedbackRequestDto requestDto = new MealFeedbackRequestDto();
            requestDto.setEatenAt(meal.getEatenAt());
            requestDto.setFoodItems(dtoList);

            String feedback = gptService.getFeedback(buildPrompt(requestDto.getEatenAt(), dtoList));
            saveFeedback(userId, feedback);
            return feedback;

        } catch (SQLException e) {
            throw new RuntimeException("식단 기반 피드백 생성 중 오류 발생", e);
        }
    }


    /** 피드백 요청 간 간격 제한 (30분) */
    private void validateFeedbackRequestInterval(Long userId) {
        feedbackDao.findLatestByUserId(userId).ifPresent(latest -> {
            LocalDateTime lastTime = latest.getCreatedAt();
            Duration gap = Duration.between(lastTime, LocalDateTime.now());

            if (gap.toMinutes() < 5) {
                throw new IllegalStateException("마지막 피드백 요청 후 5분이 지나야 새로운 피드백을 받을 수 있습니다.");
            }
        });
    }


    private String buildPrompt(LocalDateTime eatenAt, List<FoodItemSaveDto> foodItems) {
        StringBuilder summary = new StringBuilder();

        summary.append("식사 시각: ").append(eatenAt).append("\n");
        summary.append("섭취한 음식 목록:\n");

        float totalCalories = 0f;

        for (FoodItemSaveDto item : foodItems) {
            summary.append("- ").append(item.getName());
            if (item.getQuantity() != null && !item.getQuantity().isEmpty()) {
                summary.append(" (").append(item.getQuantity()).append(")");
            }
            if (item.getCalories() != null) {
                summary.append(" : ").append(item.getCalories()).append(" kcal");
                totalCalories += item.getCalories();
            }
            summary.append("\n");
        }

        summary.append("\n총 칼로리: ").append(totalCalories).append(" kcal\n");

        return """
아래는 사용자가 한 끼 동안 섭취한 음식 목록입니다.

[식단 요약]
%s

이 식단이 다이어트 또는 건강 관리에 적절했는지 알려주세요.

- 좋은 점과 나쁜 점을 구분해서 알려주세요.
- 부족하거나 과한 영양소가 있다면 지적해주세요.
- 자연스럽고 친절한 문장으로 작성해주세요.
- 마지막에 점수를 매겨주세요. (예: 점수: 85점)
""".formatted(summary.toString());
    }

    /** 특정 유저의 피드백 리스트 조회 */
    public List<FeedbackLog> getFeedbackLogs(Long userId) {
        return feedbackDao.findByUserId(userId);
    }

    /** 특정 유저의 오늘자 피드백 단건 조회 */
    public FeedbackLog getTodayFeedback(Long userId) {
        return feedbackDao.findByUserIdAndDate(userId, LocalDate.now()).orElse(null);
    }

    // 오늘자 피드백 생성

    public String generateFeedbackFromTodayMeal(Long userId) {
        validateFeedbackRequestInterval(userId); // 🔒 요청 간격 제한

        return mealDao.findLatestByUserIdAndDate(userId.intValue(), LocalDate.now())
                .map(meal -> {
                    try {
                        List<FoodItem> foodItems = foodItemDao.findByMealId(meal.getMealId());
                        List<FoodItemSaveDto> dtoList = foodItems.stream()
                                .map(f -> new FoodItemSaveDto(f.getName(), f.getCalories(), f.getQuantity()))
                                .collect(Collectors.toList());

                        String feedback = gptService.getFeedback(buildPrompt(meal.getEatenAt(), dtoList));
                        saveFeedback(userId, feedback);
                        return feedback;

                    } catch (SQLException e) {
                        throw new RuntimeException("오늘 식단 기반 피드백 생성 중 오류 발생", e);
                    }
                })
                .orElseThrow(() -> new IllegalStateException("오늘 등록된 식단이 없습니다."));
    }

}
