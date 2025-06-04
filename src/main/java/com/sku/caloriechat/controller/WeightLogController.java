package com.sku.caloriechat.controller;

import com.sku.caloriechat.dto.weightLog.WeightLogRequestDto;
import com.sku.caloriechat.dto.weightLog.WeightLogResponseDto;
import com.sku.caloriechat.service.WeightLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weight-log")
@Tag(name = "WeightLog", description = "몸무게 기록 API")
public class WeightLogController {

    private final WeightLogService weightLogService;

    @PostMapping
    @Operation(summary = "몸무게 기록 추가", description = "사용자가 하루의 몸무게를 기록합니다.")
    public ResponseEntity<Void> addWeightLog(@RequestBody WeightLogRequestDto dto) {
        weightLogService.addWeightLog(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "몸무게 기록 조회", description = "사용자의 전체 몸무게 기록을 조회합니다.")
    public ResponseEntity<List<WeightLogResponseDto>> getLogs(@RequestParam Long userId) {
        return ResponseEntity.ok(weightLogService.getLogs(userId));
    }
}
