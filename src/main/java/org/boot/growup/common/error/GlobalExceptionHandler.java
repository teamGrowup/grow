package org.boot.growup.common.error;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.constant.BaseResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.boot.growup.common.error.ErrorCode.INTERNAL_SERVER_ERROR;
import static org.boot.growup.common.error.ErrorCode.INVALID_VALUE;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<?>> handleBaseException(BaseException e) {
        return ResponseEntity
                .status(e.getCode())
                .body(new BaseResponse<>(e));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<BaseResponse<?>> handleIOException(IOException e, HttpServletRequest request) {
        log.error("[IOException] url: {}", request.getRequestURL(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse<>(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        // 필드별로 발생한 오류를 수집
        Map<String, List<String>> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                error -> Objects.requireNonNullElse(error.getDefaultMessage(), "").trim(),
                                Collectors.toList()
                        )
                ));

        // 수집된 오류를 원하는 형태로 변환
        List<Map<String, String>> errors = fieldErrors.entrySet().stream()
                .map(entry -> Map.of(entry.getKey(), String.join(", ", entry.getValue())))
                .collect(Collectors.toList());

        // ValidationErrorResponse 객체 생성
        ValidationErrorResponse response =
                new ValidationErrorResponse(INVALID_VALUE.getCode(), false, errors);

        return ResponseEntity
                .status(INVALID_VALUE.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<?>> handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletRequest request) {
        log.error("[IllegalArgumentException] url: {}", request.getRequestURL(), e);
        return ResponseEntity.badRequest().body(new BaseResponse<>(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<?>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e, HttpServletRequest request) {
        log.error("[MissingServletRequestParameterException Exception] url: {}", request.getRequestURL(), e);
        return ResponseEntity.badRequest().body(new BaseResponse<>(ErrorCode.BAD_REQUEST));
    }

    // 이외 Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleException(
            Exception e, HttpServletRequest request) {
        log.error("[Common Exception] url: {}", request.getRequestURL(), e);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(new BaseResponse<>(INTERNAL_SERVER_ERROR));
    }
}
