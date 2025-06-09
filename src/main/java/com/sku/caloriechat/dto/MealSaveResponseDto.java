package com.sku.caloriechat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealSaveResponseDto {
    private int mealId;        // 저장된 PK 반환
}