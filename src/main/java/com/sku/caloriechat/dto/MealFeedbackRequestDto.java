package com.sku.caloriechat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealFeedbackRequestDto {
    private LocalDateTime eatenAt;
    private List<FoodItemSaveDto> foodItems;
}
