package com.sku.caloriechat.domain;

import com.sku.caloriechat.enums.ActivityLevel;
import com.sku.caloriechat.enums.TargetLossSpeed;
import com.sku.caloriechat.enums.UserStatus;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userId;
    private UserStatus status;
    private String userName;
    private String email;
    private String password;
    private String gender;
    private Integer age;
    private BigDecimal height;
    private BigDecimal weight;
    private ActivityLevel activityLevel;
    private BigDecimal goalWeight;
    private TargetLossSpeed targetLossSpeed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}