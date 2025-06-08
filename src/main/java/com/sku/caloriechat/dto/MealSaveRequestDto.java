package com.sku.caloriechat.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class MealSaveRequestDto {
    private String mealTime;               // "아침"·"점심"·"저녁" …
    private LocalDateTime eatenAt;         // 식사 시각
    private Float totalCalories;           // 옵션
    private List<FoodItemSaveDto> foodItems;
}
