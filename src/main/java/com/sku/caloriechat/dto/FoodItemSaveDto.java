package com.sku.caloriechat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemSaveDto {
    private String name;
    private Float calories;
    private String quantity;
}
