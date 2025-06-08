package com.sku.caloriechat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackLog {
    private Long id;
    private Long userId;
    private LocalDate date;
    private String feedback;
    private LocalDateTime createdAt;
}
