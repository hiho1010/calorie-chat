package com.sku.caloriechat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealSaveRequestDto {

    @Schema(description = "식사 시간대", example = "아침")
    private String mealTime;

    @Schema(description = "식사 일시", example = "2024-06-10T07:30:00")
    private LocalDateTime eatenAt;

    @Schema(description = "총 섭취 칼로리", example = "550.0")
    private Float totalCalories;

    @Schema(description = "음식 목록")
    private List<FoodItemSaveDto> foodItems;
}