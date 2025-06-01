package com.sku.caloriechat.dto.calorieCalculator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

// 사용자의 신체 정보를 전달받을 Dto
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 신체 정보")
@ToString(exclude = {"user"})
public class ProfileRequestDto {
    
    @NotBlank (message = "성별 입력은 필수입니다.")
    @Schema(description = "성별 (male/female)", example = "male", required = true)
    private String gender;

    @NotNull (message = "나이 입력은 필수입니다.")
    @Schema(description = "나이", example = "25", required = true)
    private int age;

    @NotNull (message = "몸무게 입력은 필수입니다.")
    @Schema(description = "몸무게 (kg)", example = "70.0", required = true)
    private double weight;

    @NotNull (message = "키 입력은 필수입니다.")
    @Schema(description = "키 (cm)", example = "175.0", required = true)
    private double height;

    @NotBlank (message = "활동수준 입력은 필수입니다.")
    @Schema(description = "활동 수준 (LOW, MODERATE, HIGH)", example = "MODERATE", required = true)
    private String activityLevel;
}
