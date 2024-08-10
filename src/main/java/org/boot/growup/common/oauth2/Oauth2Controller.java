package org.boot.growup.common.oauth2;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.oauth2.google.GoogleOauthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class Oauth2Controller {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/login/oauth2/code/google")
    public String handleGoogleRedirect(@RequestParam("code") String code) {
        log.info("Authorization Code : {}", code);
        return "login";
    }
}
