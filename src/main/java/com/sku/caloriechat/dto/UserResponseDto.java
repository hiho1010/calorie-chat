package com.sku.caloriechat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserResponseDto(

    @Schema(description = "사용자 ID", example = "1")
    Long userId,

    @Schema(description = "사용자 이름", example = "홍길동")
    String userName,

    @Schema(description = "이메일", example = "test@example.com")
    String email,

    @Schema(description = "성별", example = "MALE")
    String gender,

    @Schema(description = "나이", example = "28")
    Integer age,

    @Schema(description = "키 (cm)", example = "175.0")
    BigDecimal height,

    @Schema(description = "몸무게 (kg)", example = "72.5")
    BigDecimal weight,

    @Schema(description = "활동 수준", example = "MODERATE")
    String activityLevel,

    @Schema(description = "목표 몸무게 (kg)", example = "68.0")
    BigDecimal goalWeight,

    @Schema(description = "감량 속도", example = "FAST")
    String targetLossSpeed,

    @Schema(description = "계정 생성일", example = "2024-06-01T12:00:00")
    LocalDateTime createdAt,

    @Schema(description = "마지막 수정일", example = "2024-06-10T15:30:00")
    LocalDateTime updatedAt
) {}