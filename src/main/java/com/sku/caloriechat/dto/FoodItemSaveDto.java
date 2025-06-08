package com.sku.caloriechat.dto;

import lombok.Data;

@Data
public class FoodItemSaveDto {
    private String name;
    private Float calories;
    private String quantity;
}
