package org.boot.growup.auth.controller;

import org.boot.growup.auth.model.dto.response.NaverAccessTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naverTokenFeignClient", url = "https://nid.naver.com/oauth2.0")
public interface NaverTokenFeignClient {
    @GetMapping("/token")
    NaverAccessTokenResponseDTO requestAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam("code") String code
    );
}
