package com.sku.caloriechat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TodayMealResponseDto {

    @Schema(description = "식단 ID", example = "12")
    private int mealId;

    @Schema(description = "식사 시간대", example = "아침")
    private String mealTime;

    @Schema(description = "식사 일시", example = "2024-06-10T08:00:00")
    private LocalDateTime eatenAt;

    @Schema(description = "총 섭취 칼로리", example = "530.5")
    private Float totalCalories;

    @Schema(description = "음식 목록")
    private List<FoodItemSaveDto> foodItems;
}