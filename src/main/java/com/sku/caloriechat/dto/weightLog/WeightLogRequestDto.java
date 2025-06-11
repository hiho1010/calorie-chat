package com.sku.caloriechat.dto.weightLog;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record WeightLogRequestDto(
    @Schema(description = "사용자 ID", example = "1")
    Long userId,

    @Schema(description = "기록 날짜", example = "2024-06-10")
    LocalDate date,

    @Schema(description = "기록한 몸무게", example = "70.5")
    float weight
) {}