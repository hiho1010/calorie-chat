package com.sku.caloriechat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealSummaryRequestDto {

    @Schema(description = "식단 요약 (ex. 아침: 바나나, 점심: 불고기, 저녁: 닭가슴살)", example = "아침: 바나나, 점심: 샐러드, 저녁: 닭가슴살")
    private String summary;
}
