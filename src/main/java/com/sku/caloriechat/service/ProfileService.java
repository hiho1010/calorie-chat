package com.sku.caloriechat.service;

import com.sku.caloriechat.converter.ProfileRequestDtoConverter;
import com.sku.caloriechat.dao.WeightLogDao;
import com.sku.caloriechat.domain.User;
import com.sku.caloriechat.domain.WeightLog;
import com.sku.caloriechat.dto.calorieCalculator.CalorieResponseDto;
import com.sku.caloriechat.dto.calorieCalculator.ProfileRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRequestDtoConverter converter;
    private final WeightLogDao weightLogDao; // ✅ 최근 몸무게 조회용 DAO 주입

    /**
     * 사용자의 성별, 키, 나이, 활동 수준, 최근 몸무게를 기반으로
     * 유지 칼로리를 구하고, 목표 감량 칼로리를 차감하여 목표 섭취 칼로리를 반환
     */
    public CalorieResponseDto calculateTargetCalories(User user) {
        ProfileRequestDto dto = converter.convert(user);

        // ✅ 최신 몸무게 가져오기
        Optional<WeightLog> latestLog = weightLogDao.findLatestByUserId(user.getUserId());
        float recentWeight = latestLog.map(WeightLog::getWeight)
                .orElse(user.getWeight().floatValue());

        // BMR 계산
        double bmr = calculateBMR(dto.getGender(), dto.getAge(), recentWeight, dto.getHeight());

        // 활동 수준에 따른 유지 칼로리
        double maintenanceCalories = bmr * getActivityMultiplier(String.valueOf(dto.getActivityLevel()));

        // 목표 감량 속도에 따른 칼로리 차감
        double deficit = getDeficitByTargetLossSpeed(String.valueOf(user.getTargetLossSpeed()));
        double targetCalories = maintenanceCalories - deficit;

        return new CalorieResponseDto(maintenanceCalories, deficit, targetCalories);
    }

    /**
     * BMR 계산 - 성별에 따라 계산 공식이 다름
     * 남자: 66 + (13.7 × 체중) + (5 × 키) − (6.8 × 나이)
     * 여자: 655 + (9.6 × 체중) + (1.8 × 키) − (4.7 × 나이)
     */
    private double calculateBMR(String gender, int age, double weight, double height) {
        if ("male".equalsIgnoreCase(gender)) {
            return 66 + (13.7 * weight) + (5.0 * height) - (6.8 * age);
        } else if ("female".equalsIgnoreCase(gender)) {
            return 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
        } else {
            throw new IllegalArgumentException("성별은 male 또는 female이어야 합니다: " + gender);
        }
    }

    /**
     * 활동 수준에 따라 곱해지는 활동 계수 반환
     * LOW = 1.2, MODERATE = 1.55, HIGH = 1.9
     */
    private double getActivityMultiplier(String activityLevel) {
        return switch (activityLevel.toUpperCase()) {
            case "LOW" -> 1.2;
            case "MODERATE" -> 1.55;
            case "HIGH" -> 1.9;
            default -> throw new IllegalArgumentException("활동 수준은 LOW, MODERATE, HIGH 중 하나여야 합니다: " + activityLevel);
        };
    }

    /**
     * 감량 속도에 따른 칼로리 차감량
     * SLOW = -300, MODERATE = -500, FAST = -700
     */
    private double getDeficitByTargetLossSpeed(String targetLossSpeed) {
        return switch (targetLossSpeed.toUpperCase()) {
            case "SLOW" -> 300.0;
            case "MODERATE" -> 500.0;
            case "FAST" -> 700.0;
            default -> 500.0; // 기본값
        };
    }
}
