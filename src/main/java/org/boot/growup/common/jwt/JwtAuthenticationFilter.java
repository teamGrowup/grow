package org.boot.growup.common.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.RequestMatcherHolder;
import org.boot.growup.common.error.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
    private final RequestMatcherHolder requestMatcherHolder;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) { //이 필터 안걸치는 path
        return requestMatcherHolder.getRequestMatchersByMinRole(null).matches(request);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
        try{
            String jwt = getJwtFromRequest(request);
            String requestURI = request.getRequestURI();
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            }
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getCode());
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getCode());
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.getCode());
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", ErrorCode.ILLEGAL_ARGUMENT_TOKEN.getCode());
        } catch (Exception e) {
            log.error("================================================");
            log.error("JwtFilter - doFilterInternal() 오류발생");
            log.error("Exception Message : {}", e.getMessage());
            log.error("Exception StackTrace : {");
            System.out.println(e);
            e.printStackTrace();
            log.error("}");
            log.error("================================================");
            request.setAttribute("exception", ErrorCode.UNKNOWN_ERROR.getCode());
        }

        filterChain.doFilter(request,response);
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
