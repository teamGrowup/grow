package org.boot.growup.common;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.enumerate.Role;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
public class RequestMatcherHolder {
    private static final List<RequestInfo> REQUEST_INFO_LIST = List.of(
            // auth
            RequestInfo.of(POST,"/customers/email/**", null),
            RequestInfo.of(POST,"/customers/oauth/**", null),
            RequestInfo.of(GET, "/login/**", null),
            RequestInfo.of(POST, "/customers/**", Role.CUSTOMER),

            // static resources
            RequestInfo.of(POST, "/v3/**", null),
            RequestInfo.of(POST, "/swagger-ui/**", null),
            RequestInfo.of(GET, "/*.ico", null),
            RequestInfo.of(GET, "/resources/**", null)
    );
    private final ConcurrentHashMap<String, RequestMatcher> reqMatcherCacheMap = new ConcurrentHashMap<>();

    /**
     * 최소 권한이 주어진 요청에 대한 RequestMatcher 반환
     * @param role 권한 (Nullable)
     * @return 생성된 RequestMatcher
     */
    public RequestMatcher getRequestMatchersByMinRole(@Nullable Role role) {
        String key = getKeyByRole(role);
        return reqMatcherCacheMap.computeIfAbsent(key, k ->
                new OrRequestMatcher(REQUEST_INFO_LIST.stream()
                        .filter(reqInfo -> Objects.equals(reqInfo.getRole(), role))
                        .map(reqInfo -> new AntPathRequestMatcher(reqInfo.getPattern(),
                                reqInfo.getMethod().name()))
                        .toArray(AntPathRequestMatcher[]::new)));
    }

    private String getKeyByRole(@Nullable Role role) {
        return role == null ? "VISITOR" : role.getKey();
    }
    @Data
    @Builder
    private static class RequestInfo {
        private HttpMethod method;
        private String pattern;
        private Role role;

        private static RequestInfo of(HttpMethod method, String pattern, Role role) {
            return RequestInfo.builder()
                    .method(method)
                    .pattern(pattern)
                    .role(role)
                    .build();
        }
    }
}
