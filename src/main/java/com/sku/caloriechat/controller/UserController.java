package com.sku.caloriechat.controller;

import com.sku.caloriechat.domain.User;
import com.sku.caloriechat.dto.*;
import com.sku.caloriechat.service.UserAuthService;
import com.sku.caloriechat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "회원 관련 API")
public class UserController {

    private final UserService     userService;
    private final UserAuthService authService;

    @Operation(summary = "회원가입", description = "이메일과 비밀번호로 회원가입을 진행합니다.")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(
        @RequestBody @Valid UserRegisterRequestDto dto) {
        return ResponseEntity.ok(userService.register(dto));
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호를 통해 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid UserLoginRequestDto dto,
        HttpServletRequest req) {

        User user = userService.authenticate(dto);

        UserDetails principal = org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getPassword())
            .build();

        Authentication auth =
            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        HttpSession session = req.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        session.setAttribute("LOGIN_USER_ID",   user.getUserId());
        session.setAttribute("LOGIN_USER_NAME", user.getUserName());
        session.setAttribute("LOGIN_USER_GOAL_WEIGHT", user.getGoalWeight());

        return ResponseEntity.ok(UserService.toResponse(user));
    }

    @Operation(summary = "로그아웃", description = "현재 세션에서 로그아웃합니다.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity
            .status(303)
            .header("Location", "/login")
            .build();
    }

    @Operation(summary = "내 정보 조회", description = "세션을 기반으로 로그인한 사용자의 정보를 반환합니다.")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(HttpSession session) {
        return ResponseEntity.ok(
            UserService.toResponse(authService.currentUser(session)));
    }

    @Operation(summary = "프로필 업데이트", description = "신체 정보 및 닉네임 등 프로필을 수정합니다.")
    @PatchMapping("/profile")
    public void updateProfile(@RequestBody UserProfileUpdateRequestDto dto,
        HttpSession session) {
        userService.updateProfile(authService.currentUserId(session), dto);
    }
}