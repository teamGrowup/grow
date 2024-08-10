package org.boot.growup.common.oauth2.google;

import org.boot.growup.common.openforeign.OpenfeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
@FeignClient(name = "googleAuthFeignClient", url = "https://accounts.google.com")
public interface GoogleAuthFeignClient {

    @PostMapping(value = "/o/oauth2/v2/auth", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String sendAuthorizationRequest(
            @RequestHeader("Content-Type") String contentType,
            @RequestBody String body
    );
}
