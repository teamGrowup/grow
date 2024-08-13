package org.boot.growup.source.seller.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.jwt.TokenDTO;
import org.boot.growup.source.seller.dto.request.SellerSignInRequestDTO;
import org.boot.growup.source.seller.dto.request.SellerSignUpRequestDTO;
import org.boot.growup.source.seller.service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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
    public ResponseEntity<BaseResponse<TokenDTO>> signIn(@Valid @RequestBody SellerSignInRequestDTO request) {
        TokenDTO response = sellerService.signIn(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @GetMapping("/role")
    public void roleTest() {
        Collection<? extends GrantedAuthority> role = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        log.info("Role : {}",role);
    }
}
