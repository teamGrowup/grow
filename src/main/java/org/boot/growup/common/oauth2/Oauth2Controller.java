package org.boot.growup.common.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.oauth2.google.GoogleClient;
import org.boot.growup.common.oauth2.google.GoogleOauthServiceImpl;
import org.boot.growup.common.oauth2.kakao.KakaoClient;
import org.boot.growup.common.oauth2.kakao.KakaoOauthServiceImpl;
import org.boot.growup.common.oauth2.naver.NaverClient;
import org.boot.growup.common.oauth2.naver.NaverOauthServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class Oauth2Controller {
    private final GoogleClient googleClient;
    private final KakaoClient kakaoClient;
    private final NaverClient naverClient;

    /**
     * Oauth2.0 로그인 페이지
     * @header null
     * @body Model
     * @response "localhost:8081/login"
     */
    @GetMapping("")
    public String login(Model model) {
        model.addAttribute("googleClientId", googleClient.getGoogleClientId());
        model.addAttribute("googleCallbackUri", googleClient.getGoogleCallbackUri());
        model.addAttribute("googleDataAccessScope", googleClient.getGoogleDataAccessScope());

        model.addAttribute("kakaoClientId", kakaoClient.getClientId());
        model.addAttribute("kakaoRedirectUri", kakaoClient.getRedirectUri());

        model.addAttribute("naverClientId", naverClient.getClientId());
        model.addAttribute("naverRedirectUri", naverClient.getRedirectUri());
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
