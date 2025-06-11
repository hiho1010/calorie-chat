package com.sku.caloriechat.dto;

import com.sku.caloriechat.enums.ActivityLevel;
import com.sku.caloriechat.enums.TargetLossSpeed;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserProfileUpdateRequestDto(

    @Schema(description = "사용자 이름", example = "홍길동")
    String userName,

    @Schema(description = "닉네임", example = "길동이")
    String nickname,

    @Schema(description = "성별", example = "MALE 또는 FEMALE")
    String gender,

    @Schema(description = "나이", example = "28")
    Integer age,

    @Schema(description = "키 (cm)", example = "175.5")
    BigDecimal height,

    @Schema(description = "몸무게 (kg)", example = "70.3")
    BigDecimal weight,

    @Schema(description = "활동량", example = "MODERATE")
    ActivityLevel activityLevel,

    @Schema(description = "목표 몸무게 (kg)", example = "65.0")
    BigDecimal goalWeight,

    @Schema(description = "감량 속도", example = "SLOW")
    TargetLossSpeed targetLossSpeed
) {}