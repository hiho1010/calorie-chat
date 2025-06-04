package com.sku.caloriechat.service;

import com.sku.caloriechat.dao.WeightLogDao;
import com.sku.caloriechat.domain.WeightLog;
import com.sku.caloriechat.dto.weightLog.WeightLogRequestDto;
import com.sku.caloriechat.dto.weightLog.WeightLogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeightLogService {

    private final WeightLogDao weightLogDao;

    // 몸무게 1개 기록 추가
    public void addWeightLog(WeightLogRequestDto dto) {
        WeightLog log = new WeightLog(
                null,
                dto.userId(),
                dto.date(),
                dto.weight(),
                LocalDateTime.now(),
                null
        );

        weightLogDao.save(log);
    }

    public List<WeightLogResponseDto> getLogs(Long userId) {
        return weightLogDao.findByUserId(userId).stream()
                .map(log -> new WeightLogResponseDto(
                        log.getWeightLogId(),
                        log.getDate(),
                        log.getWeight()
                )).toList();
    }
}
