package com.sku.caloriechat.service;

import com.sku.caloriechat.dao.FoodItemDao;
import com.sku.caloriechat.dao.MealDao;
import com.sku.caloriechat.domain.FoodItem;
import com.sku.caloriechat.domain.Meal;
import com.sku.caloriechat.dto.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealDao mealDao;
    private final FoodItemDao foodItemDao;

    @Transactional
    public MealSaveResponseDto saveMeal(int userId, MealSaveRequestDto dto) throws Exception {

        // 1) Meal 엔티티 준비 & 저장
        Meal meal = new Meal(
            0,                       // mealId (auto)
            userId,
            dto.getMealTime(),
            dto.getEatenAt(),
            dto.getTotalCalories(),
            LocalDateTime.now(),
            null,
            null
        );
        int mealId = mealDao.insert(meal);

        // 2) FoodItem 엔티티 준비 & 일괄 저장
        List<FoodItem> items = dto.getFoodItems().stream()
            .map(fi -> {
                FoodItem entity = new FoodItem();
                entity.setMealId(mealId);
                entity.setName(fi.getName());
                entity.setCalories(fi.getCalories());
                entity.setQuantity(fi.getQuantity());
                entity.setCreatedAt(LocalDateTime.now());
                return entity;
            }).collect(Collectors.toList());

        foodItemDao.batchInsert(items);

        return new MealSaveResponseDto(mealId);
    }
}