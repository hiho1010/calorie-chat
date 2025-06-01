package com.sku.caloriechat.controller;

import com.sku.caloriechat.domain.User;
import com.sku.caloriechat.dto.calorieCalculator.CalorieResponseDto;
import com.sku.caloriechat.service.ProfileService;
import com.sku.caloriechat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "사용자의 프로필 기반 칼로리 계산 API")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping("/target-calories")
    @Operation(
            summary = "목표 칼로리 계산",
            description = "userId를 기반으로 해당 사용자의 프로필 정보를 조회하여 하루 목표 섭취 칼로리를 계산합니다."
    )
    public ResponseEntity<CalorieResponseDto> calculateTargetCalories(
            @Parameter(description = "사용자 ID", example = "1", required = true)
            @RequestParam Long userId) {

        User user = userService.findById(userId);
        CalorieResponseDto response = profileService.calculateTargetCalories(user);
        return ResponseEntity.ok(response);
    }
}