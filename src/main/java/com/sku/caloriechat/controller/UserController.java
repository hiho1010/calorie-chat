package com.sku.caloriechat.controller;

import com.sku.caloriechat.domain.User;
import com.sku.caloriechat.dto.*;
import com.sku.caloriechat.service.UserAuthService;
import com.sku.caloriechat.service.UserService;
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
public class UserController {

    private final UserService     userService;
    private final UserAuthService authService;

    /* ───── 회원가입 ───── */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(
        @RequestBody @Valid UserRegisterRequestDto dto) {
        return ResponseEntity.ok(userService.register(dto));
    }

    /* ───── 로그인 & 세션 ───── */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid UserLoginRequestDto dto,
        HttpServletRequest req) {

        // ① 아이디·비밀번호 검증
        User user = userService.authenticate(dto);

        // ② Principal 생성 (권한이 필요하면 .roles("USER") 등 지정)
        UserDetails principal = org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getPassword())
            .build();

        // ③ Authentication & SecurityContext
        Authentication auth =
            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // ④ 세션에 SecurityContext + **추가 정보** 저장
        HttpSession session = req.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        // 🔽 여기 두 줄을 추가하세요
        session.setAttribute("LOGIN_USER_ID",   user.getUserId());
        session.setAttribute("LOGIN_USER_NAME", user.getUserName()); // null 가능

        // ⑤ 클라이언트에 응답
        return ResponseEntity.ok(UserService.toResponse(user));
    }

    /* ───── 로그아웃 ───── */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        authService.logout(session);           // 세션·SecurityContext 제거
        return ResponseEntity
            .status(303)                   // SEE_OTHER
            .header("Location", "/login")  // 브라우저에 /login 으로 이동 지시
            .build();
    }

    /* ───── 내 프로필(조회) ───── */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(HttpSession session) {
        return ResponseEntity.ok(
            UserService.toResponse(authService.currentUser(session)));
    }

    /* ───── 프로필 업데이트(닉네임·신체치수 등) ───── */
    @PatchMapping("/profile")
    public void updateProfile(@RequestBody UserProfileUpdateRequestDto dto,
        HttpSession session) {
        userService.updateProfile(authService.currentUserId(session), dto);
    }
}