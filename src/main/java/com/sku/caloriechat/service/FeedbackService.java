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
        String prompt = buildPrompt(requestDto.getEatenAt(), requestDto.getFoodItems());
        String feedback = gptService.getFeedback(prompt);
        saveFeedback(userId, feedback);
    }

    /** 저장된 mealId 기반 GPT 피드백 생성 */
    public String generateFeedbackFromMeal(Long userId, Long mealId) {
        try {
            Meal meal = mealDao.findById(mealId.intValue());
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
}
