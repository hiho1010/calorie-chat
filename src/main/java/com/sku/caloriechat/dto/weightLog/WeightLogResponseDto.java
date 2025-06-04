package com.sku.caloriechat.dto.weightLog;

import java.time.LocalDate;

public record WeightLogResponseDto(
        Long weightLogId,
        LocalDate date,
        float weight
) {}
