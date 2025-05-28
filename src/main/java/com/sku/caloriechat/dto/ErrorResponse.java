package com.sku.caloriechat.dto;

import com.sku.caloriechat.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final int    status;     // 404 …
    private final String errorCode;  // 1001 …
    private final String message;    // 설명
    private final String sessionId;  // nullable

    /* -------- 정적 팩토리 -------- */
    public static ErrorResponse of(ErrorCode ec, String sessionId) {
        return new ErrorResponse(ec.getHttpStatus().value(),
            ec.getCode(),
            ec.getMessage(),
            sessionId);
    }

    public static ErrorResponse of(HttpStatus status, String message, String sessionId) {
        return new ErrorResponse(status.value(),
            status.toString(),
            message,
            sessionId);
    }
}