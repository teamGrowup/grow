package org.boot.growup.common.oauth2.google.client;

import org.boot.growup.common.oauth2.google.dto.GoogleAccountResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleUserInfoFeignClient", url = "https://www.googleapis.com")
public interface GoogleUserInfoFeignClient {
    @GetMapping("/userinfo/v2/me")
    GoogleAccountResponseDTO requestGoogleAccount(
            @RequestHeader("Authorization") String authorizationHeader
    );
}
