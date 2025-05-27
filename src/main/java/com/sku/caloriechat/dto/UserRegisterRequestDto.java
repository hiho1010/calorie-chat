// dto/UserRegisterRequestDto.java
package com.sku.caloriechat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "회원 가입 요청")
public record UserRegisterRequestDto(
    @NotBlank String userName,
    @Email @NotBlank String email,
    @Size(min = 8, max = 60) String password,          // BCrypt 60자까지
    @Pattern(regexp = "MALE|FEMALE") String gender,
    @Positive @Max(120) Integer age,
    @Positive BigDecimal height,                       // cm
    @Positive BigDecimal weight,                       // kg
    @NotBlank String activityLevel,
    @Positive BigDecimal goalWeight,
    @NotBlank String targetLossSpeed
) {}