package org.boot.growup.common.oauth2.google.call;

import org.boot.growup.common.oauth2.google.dto.GoogleAccessTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleTokenFeignClient", url = "https://oauth2.googleapis.com")
public interface GoogleTokenFeignClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleAccessTokenResponseDTO requestAccessToken(
            @RequestHeader("Content-Type") String contentType,
            @RequestBody String body
    );
}
