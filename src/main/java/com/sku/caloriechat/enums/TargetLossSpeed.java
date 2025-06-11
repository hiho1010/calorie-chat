package com.sku.caloriechat.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감량 목표 속도")
public enum TargetLossSpeed {

    @Schema(description = "느린 감량 (건강하게 천천히)")
    SLOW,

    @Schema(description = "보통 속도 감량")
    MODERATE,

    @Schema(description = "빠른 감량 (짧은 기간 내 목표 달성)")
    FAST
}