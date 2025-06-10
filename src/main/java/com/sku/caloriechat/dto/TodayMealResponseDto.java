package com.sku.caloriechat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TodayMealResponseDto {
    private int mealId;
    private String mealTime;
    private LocalDateTime eatenAt;
    private Float totalCalories;
    private List<FoodItemSaveDto> foodItems;
}

