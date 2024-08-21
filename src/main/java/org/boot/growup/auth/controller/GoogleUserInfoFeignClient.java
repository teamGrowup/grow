package org.boot.growup.auth.controller;

import org.boot.growup.auth.model.dto.response.GoogleAccountResponseDTO;
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
