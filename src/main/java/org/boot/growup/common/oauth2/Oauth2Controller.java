package org.boot.growup.common.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.oauth2.google.GoogleOauthService;
import org.boot.growup.common.oauth2.kakao.KakaoOauthService;
import org.boot.growup.common.oauth2.naver.NaverOauthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
@Slf4j
public class Oauth2Controller {
    private final GoogleOauthService googleOauthService;
    private final KakaoOauthService kakaoOauthService;
    private final NaverOauthService naverOauthService;
    @GetMapping("")
    public String login(Model model) {
        model.addAttribute("googleClientId", googleOauthService.getGoogleClientId());
        model.addAttribute("googleCallbackUri", googleOauthService.getGoogleCallbackUri());
        model.addAttribute("googleDataAccessScope", googleOauthService.getGoogleDataAccessScope());

        model.addAttribute("kakaoClientId", kakaoOauthService.getClientId());
        model.addAttribute("kakaoRedirectUri", kakaoOauthService.getRedirectUri());

        model.addAttribute("naverClientId", naverOauthService.getClientId());
        model.addAttribute("naverRedirectUri", naverOauthService.getRedirectUri());
        return "login";
    }
    @GetMapping("/oauth2/code/google")
    public String handleGoogleRedirect(@RequestParam("code") String code) {
        log.info("Authorization Code : {}", code);
        return "login";
    }

    @GetMapping("/kakao/callback")
    public String handleKakaoRedirect(@RequestParam("code") String code) {
        log.info("Authorization Code : {}", code);
        return "login";
    }

    @GetMapping("/naver/callback")
    public String handleNaverRedirect(@RequestParam("code") String code) {
        log.info("Authorization Code : {}", code);
        return "login";
    }
}
