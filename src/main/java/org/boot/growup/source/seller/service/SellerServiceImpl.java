package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.error.BaseException;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.common.jwt.JwtTokenProvider;
import org.boot.growup.common.jwt.TokenDTO;
import org.boot.growup.common.userdetail.CustomUserDetailService;
import org.boot.growup.source.seller.dto.request.SellerSignInRequestDTO;
import org.boot.growup.source.seller.dto.request.SellerSignUpRequestDTO;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.boot.growup.source.seller.persist.repository.SellerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerServiceImpl implements SellerService {
    private final PasswordEncoder passwordEncoder;
    private final SellerRepository sellerRepository;
    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public void signUp(SellerSignUpRequestDTO request) {
        /* 비밀번호 암호화 */
        String encodedPassword = encodingPassword(request);
        log.info("SignUp Method => before pw : {} | after store pw : {}"
                , request.getCpPassword()
                , encodedPassword);
        /* 데이터 삽입 */
        Seller newSeller = Seller.of(request, encodedPassword);
        sellerRepository.save(newSeller);
    }

    public String encodingPassword(SellerSignUpRequestDTO request){
        return passwordEncoder.encode(request.getCpPassword());
    }

    @Transactional
    @Override
    public TokenDTO signIn(SellerSignInRequestDTO request) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(request.getCpEmail());

        if(!checkPassword(request.getCpPassword(), userDetails.getPassword())){ // 비밀번호 비교
            throw new BaseException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
