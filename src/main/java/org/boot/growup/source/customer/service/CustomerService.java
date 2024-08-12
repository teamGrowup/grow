package org.boot.growup.source.customer.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
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
import org.boot.growup.common.oauth2.google.dto.GoogleAccountResponseDTO;
import org.boot.growup.common.userdetail.CustomUserDetailService;
import org.boot.growup.source.customer.dto.request.CustomerSignInRequestDTO;
import org.boot.growup.source.customer.dto.request.CustomerSignUpRequestDTO;
import org.boot.growup.source.customer.dto.request.EmailCheckRequestDTO;
import org.boot.growup.source.customer.dto.request.GoogleAdditionalInfoRequestDTO;
import org.boot.growup.source.customer.dto.response.EmailCheckResponseDTO;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.customer.persist.repository.CustomerRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.boot.growup.common.error.ErrorCode.*;

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
    private final HttpSession session;

    @Transactional
    public void signUp(CustomerSignUpRequestDTO request) {
        /* 비밀번호 암호화 */
        String encodedPassword = encodingPassword(request);
        log.info("SignUp Method => before pw : {} | after store pw : {}"
                , request.getPassword()
                , encodedPassword);
        /* 데이터 삽입 */
        Customer newCustomer = Customer.of(request, encodedPassword);
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

    @Transactional
    public TokenDto googleSignIn(GoogleAccountResponseDTO googleAccount) {
        return customerRepository
                .findByEmailAndProvider(googleAccount.getEmail(), Provider.GOOGLE)
                .map(customer -> { // 고객이 존재하는 경우
                    UserDetails userDetails = customUserDetailService.loadUserByUsername(googleAccount.getEmail());
                    return jwtTokenProvider.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
                })
                .orElseGet(() -> { // 고객이 존재하지 않는 경우
                    try{
                        session.setAttribute("googleAccount", googleAccount); // 세션에 구글 사용자 정보 저장
                    }catch (Exception e) {
                        log.error("Session 저장 Error : ", e);
                        throw new BaseException(SESSION_SAVE_FAILED);
                    }
                    throw new BaseException(NEED_TO_GIVE_ADDITIONAL_INFORMATION);
                });

    }

    @Transactional
    public TokenDto signUpByGoogle(GoogleAdditionalInfoRequestDTO request) {
        GoogleAccountResponseDTO googleAccount = (GoogleAccountResponseDTO) session.getAttribute("googleAccount");
        if(googleAccount == null) {
            throw new BaseException(SESSION_NOT_FOUND);
        }
        /* 데이터 삽입 */
        Customer newCustomer = Customer.of(request, googleAccount);
        customerRepository.save(newCustomer);

        UserDetails userDetails = customUserDetailService.loadUserByUsername(googleAccount.getEmail());

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }
}
