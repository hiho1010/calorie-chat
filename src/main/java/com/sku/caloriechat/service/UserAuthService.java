package com.sku.caloriechat.service;

import com.sku.caloriechat.domain.User;
import com.sku.caloriechat.dto.UserLoginRequestDto;
import com.sku.caloriechat.dto.UserResponseDto;
import com.sku.caloriechat.enums.ErrorCode;
import com.sku.caloriechat.exception.CustomException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private static final String KEY = "loginUserId";
    private final UserService userService;

    /* 로그인 + 세션 저장 */
    public UserResponseDto login(UserLoginRequestDto dto, HttpSession session) {
        User user = userService.loginForAuth(dto);
        session.setAttribute(KEY, user.getUserId());           // JSESSIONID 쿠키 발급
        return UserService.toResponse(user);
    }

    /* 로그아웃 */
    public void logout(HttpSession session) { session.invalidate(); }

    /* 현재 로그인 ID */
    public Long currentUserId(HttpSession session) {
        Object v = session.getAttribute(KEY);
        if (v == null) throw new CustomException(ErrorCode.UNAUTHORIZED);
        return (Long) v;
    }

    /* 현재 로그인 사용자 객체 */
    public User currentUser(HttpSession session) {
        return userService.findById(currentUserId(session));
    }
}