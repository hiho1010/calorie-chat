// service/UserService.java
package com.sku.caloriechat.service;

import com.sku.caloriechat.dao.UserDao;
import com.sku.caloriechat.domain.User;
import com.sku.caloriechat.dto.*;
import com.sku.caloriechat.exception.CustomException;
import com.sku.caloriechat.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    /* 회원 가입 */
    public UserResponseDto register(UserRegisterRequestDto dto) {
        if (userDao.existsByEmail(dto.email()))
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);

        String hashed = BCrypt.hashpw(dto.password(), BCrypt.gensalt());

        User user = new User(
            null,
            null,
            dto.userName(),
            dto.email(),
            hashed,
            dto.gender(),
            dto.age(),
            dto.height(),
            dto.weight(),
            dto.activityLevel(),
            dto.goalWeight(),
            dto.targetLossSpeed(),
            LocalDateTime.now(),
            null,
            null
        );

        long id = userDao.save(user);
        User saved = userDao.findByEmail(dto.email())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return toResponse(saved);
    }

    /* 로그인 */
    public UserResponseDto login(UserLoginRequestDto dto) {
        User user = userDao.findByEmail(dto.email())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!BCrypt.checkpw(dto.password(), user.getPassword()))
            throw new CustomException(ErrorCode.INVALID_PASSWORD);

        return toResponse(user);
    }

    /* ---------------- private helpers ---------------- */

    private UserResponseDto toResponse(User u) {
        return new UserResponseDto(
            u.getUserId(), u.getUserName(), u.getEmail(), u.getGender(),
            u.getAge(), u.getHeight(), u.getWeight(), u.getActivityLevel(),
            u.getGoalWeight(), u.getTargetLossSpeed(),
            u.getCreatedAt(), u.getUpdatedAt()
        );
    }

    // 유저 찾기
    public User findById(Long userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
    }
}