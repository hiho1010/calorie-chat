package com.sku.caloriechat.controller;

import com.sku.caloriechat.dto.*;
import com.sku.caloriechat.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}