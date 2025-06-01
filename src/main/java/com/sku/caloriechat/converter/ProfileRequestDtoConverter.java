package com.sku.caloriechat.converter;

import com.sku.caloriechat.domain.User;
import com.sku.caloriechat.dto.calorieCalculator.ProfileRequestDto;
import org.springframework.stereotype.Component;

@Component
public class ProfileRequestDtoConverter {

    public ProfileRequestDto convert(User user) {
        return new ProfileRequestDto(
                user.getGender(),
                user.getAge(),
                user.getWeight().doubleValue(),
                user.getHeight().doubleValue(),
                user.getActivityLevel()
        );
    }
}
