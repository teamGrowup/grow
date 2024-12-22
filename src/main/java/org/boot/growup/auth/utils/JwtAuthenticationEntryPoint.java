package org.boot.growup.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.common.constant.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

import static org.boot.growup.common.constant.ErrorCode.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint { // 인증되지 않은 사용자가 보호된 리소스 접근 시 발생하는 예외를 처리
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // request에서 exception 코드 가져오기
        ErrorCode exceptionCode = (ErrorCode) request.getAttribute("exception");
        sendErrorResponse(response, Objects.requireNonNullElse(exceptionCode, UNKNOWN_ERROR));
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException{
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        BaseResponse<?> errorResponse = new BaseResponse<>(errorCode);
        new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    }
}
