package org.boot.growup.source.seller.service;

import org.boot.growup.common.jwt.TokenDTO;
import org.boot.growup.source.seller.dto.request.SellerSignInRequestDTO;
import org.boot.growup.source.seller.dto.request.SellerSignUpRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface SellerService {
    /*
    판매자 이메일 회원가입
     */
    void signUp(SellerSignUpRequestDTO request);

    /*
    판매자 이메일 로그인
     */
    TokenDTO signIn(SellerSignInRequestDTO request);
}
