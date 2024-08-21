package org.boot.growup.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.service.SellerService;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.auth.model.dto.request.SellerSignInRequestDTO;
import org.boot.growup.auth.model.dto.request.SellerSignUpRequestDTO;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    /**
     * [POST]
     * 판매자 이메일 회원가입
     * @header null
     * @body SellerSignUpRequestDTO
     * @response void
     */
    @PostMapping("/email/register")
    public void signUp(@Valid @RequestBody SellerSignUpRequestDTO request) {
        sellerService.signUp(request);
    }

    /**
     * [POST]
     * 판매자 이메일 로그인
     * @header null
     * @body SellerSignInRequestDTO
     * @response TokenDTO
     */
    @PostMapping("/email/login")
    public BaseResponse<TokenDTO> signIn(@Valid @RequestBody SellerSignInRequestDTO request) {
        TokenDTO response = sellerService.signIn(request);
        return new BaseResponse<>(response);
    }
}
