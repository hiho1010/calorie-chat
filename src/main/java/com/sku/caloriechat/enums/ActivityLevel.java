package com.sku.caloriechat.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자의 활동 수준")
public enum ActivityLevel {

    @Schema(description = "낮은 활동량 (주로 앉아서 생활)")
    LOW,

    @Schema(description = "보통 활동량 (가벼운 운동 또는 활동)")
    MODERATE,

    @Schema(description = "높은 활동량 (자주 운동 또는 육체 노동)")
    HIGH
}