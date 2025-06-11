package com.sku.caloriechat.dto;

import com.sku.caloriechat.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "HTTP 상태 코드", example = "404")
    private final int status;

    @Schema(description = "비즈니스 에러 코드", example = "1001")
    private final String errorCode;

    @Schema(description = "에러 메시지", example = "해당 사용자가 존재하지 않습니다.")
    private final String message;

    @Schema(description = "세션 ID (옵션)", example = "abc123session")
    private final String sessionId;

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