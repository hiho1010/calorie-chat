package com.sku.caloriechat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemSaveDto {

    @Schema(description = "음식명", example = "닭가슴살")
    private String name;

    @Schema(description = "칼로리", example = "220.5")
    private Float calories;

    @Schema(description = "섭취량", example = "150g")
    private String quantity;
}