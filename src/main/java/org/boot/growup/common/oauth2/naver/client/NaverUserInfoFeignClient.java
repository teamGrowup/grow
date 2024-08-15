package org.boot.growup.common.oauth2.naver.client;

import org.boot.growup.common.oauth2.naver.dto.NaverAccountResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naverUserInfoFeignClient", url = "https://openapi.naver.com")
public interface NaverUserInfoFeignClient {
    @GetMapping("/v1/nid/me")
    NaverAccountResponseDTO requestNaverAccount(
            @RequestHeader("Authorization") String authorizationHeader
    );
}
