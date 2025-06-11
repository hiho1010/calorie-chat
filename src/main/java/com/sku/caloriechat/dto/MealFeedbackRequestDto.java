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
public class MealFeedbackRequestDto {

    @Schema(description = "식사 시간", example = "2024-06-10T08:00:00")
    private LocalDateTime eatenAt;

    @Schema(description = "음식 목록")
    private List<FoodItemSaveDto> foodItems;
}