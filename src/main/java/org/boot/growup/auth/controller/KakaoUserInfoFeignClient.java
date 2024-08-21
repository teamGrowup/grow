package org.boot.growup.auth.controller;

import org.boot.growup.auth.model.dto.response.KakaoAccountResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoUserInfoFeignClient", url = "https://kapi.kakao.com")
public interface KakaoUserInfoFeignClient {
    @GetMapping("/v2/user/me")
    KakaoAccountResponseDTO requestKakaoAccount(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestHeader("Content-Type") String contentTypeHeader
    );
}
