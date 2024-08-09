package org.boot.growup.source.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.enumerate.Gender;
import org.boot.growup.common.enumerate.Role;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.common.jwt.JwtTokenProvider;
import org.boot.growup.common.jwt.TokenDto;
import org.boot.growup.common.oauth2.Provider;
import org.boot.growup.common.userdetail.CustomUserDetailService;
import org.boot.growup.source.customer.dto.request.CustomerEmailSignInRequest;
import org.boot.growup.source.customer.dto.request.CustomerEmailSignUpRequest;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.customer.persist.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(CustomerEmailSignUpRequest request) {
        /* 비밀번호 암호화 */
        String encodedPassword = encodingPassword(request);
        log.info("SignUp Method => before pw : {} | after store pw : {}"
                , request.password()
                , encodedPassword);
        /* 데이터 삽입 */
        Customer newCustomer = Customer.builder()
                .email(request.email())
                .password(encodedPassword)
                .phoneNumber(request.phoneNumber())
                .birthday(request.birthday())
                .gender(Gender.valueOf(request.gender().name()))
                .address(request.address())
                .nickname(request.nickname())
                .name(request.name())
                .provider(Provider.EMAIL)
                .role(Role.CUSTOMER)
                .build();
        customerRepository.save(newCustomer);
    }


    public String encodingPassword(CustomerEmailSignUpRequest request){
        return passwordEncoder.encode(request.password());
    }

    @Transactional
    public TokenDto signIn(CustomerEmailSignInRequest request) {
        UserDetails userDetails =
                customUserDetailService.loadUserByUsername(request.email());

        if(!checkPassword(request.password(), userDetails.getPassword())){ // 비밀번호 비교
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        Customer customer = customerRepository.findByEmail(request.email()).orElseThrow(
                    () -> new BaseException(ErrorCode.USER_NOT_FOUND));

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
