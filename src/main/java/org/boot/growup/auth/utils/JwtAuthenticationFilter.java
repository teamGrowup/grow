package org.boot.growup.auth.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.utils.RequestMatcherHolder;
import org.boot.growup.common.constant.ErrorCode;
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
    private final HttpSession session;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) { //이 필터 안걸치는 path

        if(request.getAttribute("alreadyReqLogged")==null){ // 중복 제거 로직
            request.setAttribute("alreadyReqLogged", true);
            // 로깅: IP 주소, 요청 URI, HTTP 메서드
            String ipAddress = request.getRemoteAddr();
            String requestUri = request.getRequestURI();
            String method = request.getMethod();
            log.info("Incoming request: IP={}, URI={}, Method={}", ipAddress, requestUri, method);
        }


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
                session.setAttribute("provider", jwtTokenProvider.getProvider(jwt));
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
