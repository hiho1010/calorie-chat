package com.sku.caloriechat.dto.weightLog;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record WeightLogResponseDto(
    @Schema(description = "몸무게 기록 ID", example = "10")
    Long weightLogId,

    @Schema(description = "기록 날짜", example = "2024-06-10")
    LocalDate date,

    @Schema(description = "몸무게", example = "70.5")
    float weight
) {}