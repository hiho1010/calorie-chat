package com.sku.caloriechat.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    private int mealId;
    private int userId;
    private String mealTime;
    private LocalDateTime eatenAt;
    private Float totalCalories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}