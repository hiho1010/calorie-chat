package com.sku.caloriechat.dto.weightLog;


import java.time.LocalDate;

public record WeightLogRequestDto(
        Long userId,
        LocalDate date,
        float weight
) {}
