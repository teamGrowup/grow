package org.boot.growup.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return (
            pathMatcher.match("/login",path) && request.getMethod().equals("GET") ||
            pathMatcher.match("/favicon.ico", path) && request.getMethod().equals("GET")
        );
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
        filterChain.doFilter(request,response);
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER); // Authorization 이름의 헤더의 내용을 가져온다.
        if (bearerToken.startsWith(TOKEN_PREFIX)) { // 헤더의 내용이 Bearer 로 시작하는지 확인
            return bearerToken.substring(TOKEN_PREFIX.length()); // Bearer 이후의 내용이 토큰임. (AT or RT)
        }
        return null;
    }
}
