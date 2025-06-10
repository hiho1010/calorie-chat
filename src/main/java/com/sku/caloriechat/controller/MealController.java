package com.sku.caloriechat.controller;

import com.sku.caloriechat.dto.*;
import com.sku.caloriechat.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users/{userId}/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ResponseEntity<MealSaveResponseDto> createMeal(
        @PathVariable int userId,
        @RequestBody MealSaveRequestDto request) throws Exception {

        MealSaveResponseDto resp = mealService.saveMeal(userId, request);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/today")
    @Operation(summary = "오늘의 식단 mealId 조회", description = "오늘 등록한 식단 중 가장 최근 것을 mealId로 반환합니다.")
    public ResponseEntity<?> getTodayMeal(@PathVariable int userId) {
        return mealService.findTodayMeal(userId)
                .<ResponseEntity<?>>map(meal -> ResponseEntity.ok(Map.of("mealId", meal.getMealId())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "오늘 등록된 식단이 없습니다.")));
    }


    @GetMapping("/today/detail")
    @Operation(summary = "오늘의 식단 상세 조회", description = "오늘 등록한 식단의 상세 정보와 음식 목록을 반환합니다.")
    public ResponseEntity<?> getTodayMealWithItems(@PathVariable int userId) {
        return mealService.getTodayMealWithItems(userId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "오늘 등록된 식단이 없습니다.")));
    }
}