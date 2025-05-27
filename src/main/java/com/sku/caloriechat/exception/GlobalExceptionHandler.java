package com.sku.caloriechat.exception;

import com.sku.caloriechat.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ---------- 비즈니스 예외 ---------- */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustom(
        CustomException ex,
        HttpServletRequest request
    ) {
        log.warn("CustomException: {}", ex.getErrorCode(), ex);
        ErrorResponse body = ErrorResponse.of(ex.getErrorCode(), sessionId(request));
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(body);
    }

    /* ---------- 예상 못한 모든 예외 ---------- */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(
        Exception ex,
        HttpServletRequest request
    ) {
        log.error("Unhandled Exception", ex);
        ErrorResponse body = ErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage(),
            sessionId(request)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /* -------- util -------- */
    private String sessionId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? session.getId() : null;
    }
}