package com.sku.caloriechat.dto;


import com.sku.caloriechat.enums.ActivityLevel;
import com.sku.caloriechat.enums.TargetLossSpeed;
import lombok.Builder;

import java.math.BigDecimal;

@Builder  // 선택 값만 채울 수 있도록
public record UserProfileUpdateRequestDto(
    String userName,
    String nickname,
    String gender,
    Integer age,
    BigDecimal height,
    BigDecimal weight,
    ActivityLevel activityLevel,
    BigDecimal goalWeight,
    TargetLossSpeed targetLossSpeed
) {}