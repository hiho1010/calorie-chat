package com.sku.caloriechat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealSaveResponseDto {

    @Schema(description = "식단 ID", example = "123")
    private int mealId;
}