package org.boot.growup.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.Oauth2Property;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class Oauth2Controller {
    private final Oauth2Property oauth2Property;

    /**
     * Oauth2.0 로그인 페이지
     * @header null
     * @body Model
     * @response "localhost:8081/login"
     */
    @GetMapping("")
    public String login(Model model) {
        model.addAttribute("googleClientId", oauth2Property.getGoogle().getClient().getId());
        model.addAttribute("googleCallbackUri", oauth2Property.getGoogle().getCallbackUri());
        model.addAttribute("googleDataAccessScope", oauth2Property.getGoogle().getAccessScope());

        model.addAttribute("kakaoClientId", oauth2Property.getKakao().getClientId());
        model.addAttribute("kakaoRedirectUri", oauth2Property.getKakao().getRedirectUri());

        model.addAttribute("naverClientId", oauth2Property.getNaver().getClientId());
        model.addAttribute("naverRedirectUri", oauth2Property.getNaver().getRedirectUri());
        return "login";
    }

    /**
     * Oauth2.0 구글 리다이렉트 > 인가코드 받기
     * @header null
     * @body null
     * @param code
     * @response "localhost:8081/oauth2/code/google?code=xxx"
     */
    @GetMapping("/oauth2/code/google")
    public String handleGoogleRedirect(@RequestParam("code") String code) {
        log.info("Authorization Code : {}", code);
        return "login";
    }

    /**
     * Oauth2.0 카카오 리다이렉트 > 인가코드 받기
     * @header null
     * @body null
     * @param code
     * @response "localhost:8081/login/kakao/callback?code=xxx"
     */
    @GetMapping("/kakao/callback")
    public String handleKakaoRedirect(@RequestParam("code") String code) {
        log.info("Authorization Code : {}", code);
        return "login";
    }

    /**
     * Oauth2.0 네이버 리다이렉트 > 인가코드 받기
     * @header null
     * @body null
     * @param code
     * @response "localhost:8081/login/naver/callback?code=xxx"
     */
    @GetMapping("/naver/callback")
    public String handleNaverRedirect(@RequestParam("code") String code) {
        log.info("Authorization Code : {}", code);
        return "login";
    }
}
