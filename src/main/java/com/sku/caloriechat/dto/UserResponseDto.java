package com.sku.caloriechat.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserResponseDto(
    Long userId,
    String userName,
    String email,
    String gender,
    Integer age,
    BigDecimal height,
    BigDecimal weight,
    String activityLevel,
    BigDecimal goalWeight,
    String targetLossSpeed,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}