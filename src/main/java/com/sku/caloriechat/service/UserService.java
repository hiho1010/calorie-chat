package com.sku.caloriechat.service;

import com.sku.caloriechat.dao.UserDao;
import com.sku.caloriechat.domain.User;
import com.sku.caloriechat.dto.*;
import com.sku.caloriechat.enums.ErrorCode;
import com.sku.caloriechat.enums.UserStatus;
import com.sku.caloriechat.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    /* ───────── 1단계: 이메일·패스워드만 회원가입 ───────── */
    public UserResponseDto register(UserRegisterRequestDto dto) {
        if (userDao.existsByEmail(dto.email()))
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);

        String hashed = BCrypt.hashpw(dto.password(), BCrypt.gensalt());
        long id = userDao.saveMinimal(dto.email(), hashed);

        return new UserResponseDto(
            id,              // userId
            null,            // userName
            dto.email(),
            null,
            null, null, null, null,
            null, null,
            LocalDateTime.now(),
            null
        );
    }

    /* ───────── 로그인(비밀번호 검증) ───────── */
    public User loginForAuth(UserLoginRequestDto dto) {
        User user = userDao.findByEmail(dto.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword()))
            throw new CustomException(ErrorCode.INVALID_PASSWORD);

        return user;   // 인증만 하고 세션 저장은 다른 Service가 담당
    }

    /* ───────── 2단계: 프로필 등록/수정 ───────── */
    @Transactional
    public void updateProfile(Long userId, UserProfileUpdateRequestDto dto) {
        User p = new User();
        p.setUserName(dto.userName());
        p.setGender(dto.gender());
        p.setAge(dto.age());
        p.setHeight(dto.height());
        p.setWeight(dto.weight());
        p.setActivityLevel(dto.activityLevel());
        p.setGoalWeight(dto.goalWeight());
        p.setTargetLossSpeed(dto.targetLossSpeed());
        userDao.updateProfile(userId, p);
    }

    /* 조회 용도 */
    public User findById(Long id) {
        return userDao.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /* 응답 DTO 변환 (필요 시 확장) */
    public static UserResponseDto toResponse(User u) {
        return new UserResponseDto(
            u.getUserId(), u.getUserName(), u.getEmail(), u.getGender(),
            u.getAge(), u.getHeight(), u.getWeight(), u.getActivityLevel().toString(),
            u.getGoalWeight(), u.getTargetLossSpeed().toString(),
            u.getCreatedAt(), u.getUpdatedAt()
        );
    }

    public User authenticate(UserLoginRequestDto dto) {
        User user = userDao.findByEmail(dto.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        return user;
    }

    /** email 로 사용자 조회 (404 → CustomException) */
    public User findByEmail(String email) {
        return userDao.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

}