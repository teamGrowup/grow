package org.boot.growup.auth.service;

import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.auth.model.dto.request.SellerSignInRequestDTO;
import org.boot.growup.auth.model.dto.request.SellerSignUpRequestDTO;
import org.boot.growup.auth.persist.entity.Seller;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SellerService extends UserDetailsService {
    /*
    판매자 이메일 회원가입
     */
    void signUp(SellerSignUpRequestDTO request);

    /*
    판매자 이메일 로그인
     */
    TokenDTO signIn(SellerSignInRequestDTO request);

    /*
    현재 로그인한 판매자 정보 가져오기.
     */
    Seller getCurrentSeller();

    /*
    Seller UserDetailsService
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}

