package org.boot.growup.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.service.SellerService;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.common.constant.Role;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.auth.utils.JwtTokenProvider;
import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.auth.service.UserService;
import org.boot.growup.auth.model.dto.request.SellerSignInRequestDTO;
import org.boot.growup.auth.model.dto.request.SellerSignUpRequestDTO;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.auth.persist.repository.SellerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final PasswordEncoder passwordEncoder;
    private final SellerRepository sellerRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

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

    public String encodingPassword(SellerSignUpRequestDTO request) {
        return passwordEncoder.encode(request.getCpPassword());
    }

    @Override
    public TokenDTO signIn(SellerSignInRequestDTO request) {
        UserDetails userDetails = userService.loadUserByUsername(request.getCpEmail());

        if (!checkPassword(request.getCpPassword(), userDetails.getPassword())) { // 비밀번호 비교
            throw new BaseException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Seller getCurrentSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        log.info(user.getUsername(), user.getAuthorities().toString());
        if (!ObjectUtils.isEmpty(user)) {
            String useremail = user.getUsername();
            String authority = user.getAuthorities().stream().findFirst().orElseThrow(
                    () -> new BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
            ).toString();

            log.info("useremail : {} | authority : {}", useremail, authority);

            if (authority.equals(Role.SELLER.getKey())) {
                return sellerRepository.findByCpEmail(useremail).orElseThrow(
                        () -> new BaseException(ErrorCode.SELLER_NOT_FOUND)
                );
            }

            throw new BaseException(ErrorCode.ACCESS_DENIED);
        }

        throw new BaseException(ErrorCode.ACCESS_DENIED);
    }
}
