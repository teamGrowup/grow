package org.boot.growup.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.error.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.boot.growup.common.error.ErrorCode.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // request에서 exception 코드 가져오기
        Integer exceptionCode = (Integer) request.getAttribute("exception");

        if (exceptionCode == null) {
            sendErrorResponse(response, UNKNOWN_ERROR);
        } else if (exceptionCode == WRONG_TYPE_TOKEN.getCode()) {
            sendErrorResponse(response, WRONG_TYPE_TOKEN);
        } else if (exceptionCode == EXPIRED_TOKEN.getCode()) {
            sendErrorResponse(response, EXPIRED_TOKEN);
        } else if (exceptionCode == UNSUPPORTED_TOKEN.getCode()) {
            sendErrorResponse(response, UNSUPPORTED_TOKEN);
        } else if (exceptionCode == ILLEGAL_ARGUMENT_TOKEN.getCode()) {
            sendErrorResponse(response, ILLEGAL_ARGUMENT_TOKEN);
        } else {
            sendErrorResponse(response, ACCESS_DENIED);
        }
    }

    private void sendErrorResponse(HttpServletResponse httpServletResponse, ErrorCode errorCode) throws IOException{
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType(APPLICATION_JSON_VALUE);

        BaseResponse<?> errorResponse = new BaseResponse(errorCode);
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorResponse);
    }
}
