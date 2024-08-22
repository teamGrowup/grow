package org.boot.growup.auth.client;

import org.boot.growup.auth.model.dto.response.KakaoAccessTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoTokenFeignClient", url = "https://kauth.kakao.com/oauth")
public interface KakaoTokenFeignClient {
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoAccessTokenResponseDTO requestAccessToken(
            @RequestHeader("Content-Type") String contentType,
            @RequestBody String body
    );
}
