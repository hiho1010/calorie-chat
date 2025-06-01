package com.sku.caloriechat.dto.calorieCalculator;


import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "하루 섭취 칼로리 계산 결과 DTO")
public record CalorieResponseDto(

        @Schema(description = "유지 칼로리 (감량 안 해도 될 때 필요한 칼로리)", example = "2200")
        double maintenanceCalories,

        @Schema(description = "감량 목표를 위한 하루 칼로리 차감량", example = "500")
        double dailyDeficit,

        @Schema(description = "최종 하루 섭취 칼로리 목표 (유지 칼로리 - 차감량)", example = "1700")
        double targetCalories
) {}
