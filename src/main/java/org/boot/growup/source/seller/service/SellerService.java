package org.boot.growup.source.seller.service;

import org.boot.growup.common.jwt.TokenDTO;
import org.boot.growup.source.seller.dto.request.SellerSignInRequestDTO;
import org.boot.growup.source.seller.dto.request.SellerSignUpRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface SellerService {
    void signUp(SellerSignUpRequestDTO request);
    TokenDTO signIn(SellerSignInRequestDTO request);
}
