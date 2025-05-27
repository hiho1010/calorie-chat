// controller/UserController.java
package com.sku.caloriechat.controller;

import com.sku.caloriechat.dto.*;
import com.sku.caloriechat.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* 회원 가입 */
    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponseDto> register(
        @RequestBody @Valid UserRegisterRequestDto dto) {
        return ResponseEntity.ok(userService.register(dto));
    }

    /* 로그인 */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(
        @RequestBody @Valid UserLoginRequestDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }
}