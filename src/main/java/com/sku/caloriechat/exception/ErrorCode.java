package com.sku.caloriechat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== 비즈니스 코드 =====
    USER_NOT_FOUND   ("1001", HttpStatus.NOT_FOUND            , "해당 사용자가 존재하지 않습니다."),
    INVALID_PASSWORD ("1002", HttpStatus.UNAUTHORIZED         , "비밀번호가 올바르지 않습니다."),
    DUPLICATE_EMAIL  ("1003", HttpStatus.CONFLICT             , "이미 사용 중인 이메일입니다."),

    // ===== 시스템 공통 =====
    INTERNAL_ERROR   ("9999", HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 오류입니다.");

    private final String     code;        // 비즈니스 코드
    private final HttpStatus httpStatus;  // HTTP 상태
    private final String     message;     // 사용자 메시지
}