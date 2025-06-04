package com.sku.caloriechat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeightLog {
    private Long weightLogId;
    private Long userId;
    private LocalDate date;
    private float weight;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
