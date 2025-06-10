package com.sku.caloriechat.controller;

import com.sku.caloriechat.domain.FeedbackLog;
import com.sku.caloriechat.dto.MealFeedbackRequestDto;
import com.sku.caloriechat.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "GPT 피드백 관리 API")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/generate")
    @Operation(
            summary = "GPT 피드백 생성 및 저장",
            description = "음식 목록과 식사 시각을 기반으로 GPT에게 피드백을 받아 저장합니다."
    )
    public ResponseEntity<String> generateFeedback(
            @RequestParam Long userId,
            @RequestBody MealFeedbackRequestDto requestDto
    ) {
        feedbackService.generateAndSaveFeedback(userId, requestDto);
        return ResponseEntity.ok("GPT feedback generated and saved.");
    }

    @PostMapping("/generate/from-meal")
    @Operation(
            summary = "저장된 식단 기반 GPT 피드백 생성",
            description = "mealId를 기반으로 DB에서 식단을 조회하여 GPT 피드백을 생성하고 반환합니다."
    )
    public ResponseEntity<String> generateFeedbackFromMeal(
            @RequestParam Long userId,
            @RequestParam Long mealId
    ) {
        String feedback = feedbackService.generateFeedbackFromMeal(userId, mealId);
        return ResponseEntity.ok(feedback);
    }


    @PostMapping("/manual")
    @Operation(
            summary = "수동 피드백 저장",
            description = "GPT 없이 수동으로 피드백 텍스트를 저장합니다."
    )
    public ResponseEntity<String> saveManualFeedback(
            @RequestParam Long userId,
            @RequestParam String feedbackText
    ) {
        feedbackService.saveFeedback(userId, feedbackText);
        return ResponseEntity.ok("Manual feedback saved successfully.");
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "유저의 피드백 전체 조회",
            description = "특정 유저의 전체 피드백 로그를 조회합니다."
    )
    public ResponseEntity<List<FeedbackLog>> getFeedbacks(@PathVariable Long userId) {
        return ResponseEntity.ok(feedbackService.getFeedbackLogs(userId));
    }

    @GetMapping("/{userId}/today")
    @Operation(
            summary = "오늘자 피드백 조회",
            description = "특정 유저의 오늘자 GPT 피드백을 단건으로 조회합니다."
    )
    public ResponseEntity<?> getTodayFeedback(@PathVariable Long userId) {
        FeedbackLog log = feedbackService.getTodayFeedback(userId);
        if (log == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "오늘자 피드백이 없습니다."));
        }
        return ResponseEntity.ok(log);
    }

}
