package com.sku.caloriechat.service;

import com.sku.caloriechat.dao.FeedbackDao;
import com.sku.caloriechat.domain.FeedbackLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackDao feedbackDao;
    private final GptService gptService;

    /** 피드백 저장 */
    public void saveFeedback(Long userId, String feedbackText) {
        FeedbackLog log = new FeedbackLog();
        log.setUserId(userId);
        log.setDate(LocalDate.now());
        log.setFeedback(feedbackText);
        log.setCreatedAt(LocalDateTime.now());

        feedbackDao.save(log);
    }

    /** GPT로 피드백 생성 후 저장 */
    public void generateAndSaveFeedback(Long userId, String mealSummary) {
        String prompt = buildPrompt(mealSummary);
        String feedback = gptService.getFeedback(prompt);
        saveFeedback(userId, feedback);
    }

    /** GPT 프롬프트 빌드 메서드 */
    private String buildPrompt(String mealSummary) {
        return  """
    아래는 사용자의 오늘 하루 식단 요약입니다.

    [식단 요약]
    %s

    이 식단이 다이어트 또는 건강 관리에 적절했는지 알려주세요.

    - 좋은 점과 나쁜 점을 구분해서 알려주세요.
    - 부족하거나 과한 영양소가 있다면 지적해주세요.
    - 너무 짧은 피드백 말고, 문장으로 자연스럽게 작성해주세요.
    - 마지막에는 이 식단을 100점 만점 기준으로 점수를 매겨주세요. (예: 점수: 85점)
    """.formatted(mealSummary);
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
