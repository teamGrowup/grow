package org.boot.growup.source.customer.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.boot.growup.common.email.EmailMessageDTO;
import org.boot.growup.common.email.EmailService;
import org.boot.growup.common.enumerate.Gender;
import org.boot.growup.common.enumerate.Role;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.common.jwt.JwtTokenProvider;
import org.boot.growup.common.jwt.TokenDto;
import org.boot.growup.common.oauth2.Provider;
import org.boot.growup.common.userdetail.CustomUserDetailService;
import org.boot.growup.source.customer.dto.request.CustomerSignInRequestDTO;
import org.boot.growup.source.customer.dto.request.CustomerSignUpRequestDTO;
import org.boot.growup.source.customer.dto.request.EmailCheckRequestDTO;
import org.boot.growup.source.customer.dto.response.EmailCheckResponseDTO;
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
    private final EmailService emailService;

    @Transactional
    public void signUp(CustomerSignUpRequestDTO request) {
        /* 비밀번호 암호화 */
        String encodedPassword = encodingPassword(request);
        log.info("SignUp Method => before pw : {} | after store pw : {}"
                , request.getPassword()
                , encodedPassword);
        /* 데이터 삽입 */
        Customer newCustomer = Customer.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .gender(Gender.valueOf(request.getGender().name()))
                .address(request.getAddress())
                .nickname(request.getNickname())
                .name(request.getName())
                .provider(Provider.EMAIL)
                .role(Role.CUSTOMER)
                .build();
        customerRepository.save(newCustomer);
    }

    public String encodingPassword(CustomerSignUpRequestDTO request){
        return passwordEncoder.encode(request.getPassword());
    }

    @Transactional
    public TokenDto signIn(CustomerSignInRequestDTO request) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(request.getEmail());

        if(!checkPassword(request.getPassword(), userDetails.getPassword())){ // 비밀번호 비교
            throw new BaseException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional
    public EmailCheckResponseDTO emailCheck(EmailCheckRequestDTO request) throws MessagingException {
        EmailMessageDTO emailMessage = EmailMessageDTO.from(request);
        String validationCode = emailService.sendMail(emailMessage);
        return EmailCheckResponseDTO.builder()
                .validationCode(validationCode)
                .build();
    }
}
