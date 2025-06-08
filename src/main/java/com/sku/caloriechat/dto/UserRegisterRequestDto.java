package com.sku.caloriechat.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestDto(

    @Email(message = "잘못된 이메일 형식입니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, max = 12, message = "비밀번호는 6~12자여야 합니다.")
    String password
) {}