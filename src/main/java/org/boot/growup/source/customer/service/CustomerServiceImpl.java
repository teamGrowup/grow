package org.boot.growup.source.customer.service;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.boot.growup.common.email.EmailMessageDTO;
import org.boot.growup.common.email.EmailService;
import org.boot.growup.common.enumerate.Role;
import org.boot.growup.common.error.BaseException;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.common.jwt.JwtTokenProvider;
import org.boot.growup.common.jwt.TokenDTO;
import org.boot.growup.common.enumerate.Provider;
import org.boot.growup.common.oauth2.google.dto.GoogleAccountResponseDTO;
import org.boot.growup.common.oauth2.kakao.dto.KakaoAccountResponseDTO;
import org.boot.growup.common.oauth2.naver.dto.NaverAccountResponseDTO;
import org.boot.growup.common.userdetail.CustomUserDetailService;
import org.boot.growup.source.customer.dto.request.*;
import org.boot.growup.source.customer.dto.response.EmailCheckResponseDTO;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.customer.persist.repository.CustomerRepository;

import org.boot.growup.source.seller.persist.entity.Seller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import static org.boot.growup.common.error.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final HttpSession session;

    @Transactional
    @Override
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
    @Override
    public TokenDTO signIn(CustomerSignInRequestDTO request) {
        UserDetails userDetails = customUserDetailService.loadUserByUsernameAndProvider(request.getEmail(), Provider.EMAIL);

        if(!checkPassword(request.getPassword(), userDetails.getPassword())){ // 비밀번호 비교
            throw new BaseException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional
    @Override
    public EmailCheckResponseDTO checkEmail(EmailCheckRequestDTO request) throws MessagingException {
        EmailMessageDTO emailMessage = EmailMessageDTO.from(request);
        String validationCode = emailService.sendMail(emailMessage);
        return EmailCheckResponseDTO.builder()
                .validationCode(validationCode)
                .build();
    }

    @Transactional
    @Override
    public TokenDTO signInGoogle(GoogleAccountResponseDTO googleAccount) {
        return customerRepository
                .findByEmailAndProvider(googleAccount.getEmail(), Provider.GOOGLE)
                .map(customer -> { // 고객이 존재하는 경우
                    UserDetails userDetails = customUserDetailService.loadUserByUsernameAndProvider(
                                googleAccount.getEmail(), Provider.GOOGLE);
                    return jwtTokenProvider.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
                })
                .orElseGet(() -> { // 고객이 존재하지 않는 경우
                    try{
                        session.setAttribute("googleAccount", googleAccount); // 세션에 구글 사용자 정보 저장
                    }catch (Exception e) {
                        log.error("Session 저장 Error : ", e);
                        log.info("googleAccount saved in session: {}", session.getAttribute("googleAccount"));
                        throw new BaseException(SESSION_SAVE_FAILED);
                    }
                    throw new BaseException(NEED_TO_GIVE_ADDITIONAL_INFORMATION);
                });

    }

    @Transactional
    @Override
    public TokenDTO signInGoogleAdditional(GoogleAdditionalInfoRequestDTO request) {
        GoogleAccountResponseDTO googleAccount = (GoogleAccountResponseDTO) session.getAttribute("googleAccount");
        if(googleAccount == null) {
            throw new BaseException(SESSION_NOT_FOUND);
        }
        /* 데이터 삽입 */
        Customer newCustomer = Customer.of(request, googleAccount);
        customerRepository.save(newCustomer);

        UserDetails userDetails = customUserDetailService.loadUserByUsernameAndProvider(
                    googleAccount.getEmail(), Provider.GOOGLE);

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    @Transactional
    @Override
    public TokenDTO signInKakao(KakaoAccountResponseDTO kakaoAccount) {
        return customerRepository
                .findByEmailAndProvider(kakaoAccount.getKakaoAccount().getEmail(), Provider.KAKAO)
                .map(customer -> { // 고객이 존재하는 경우
                    UserDetails userDetails = customUserDetailService
                                .loadUserByUsernameAndProvider(
                                            kakaoAccount.getKakaoAccount().getEmail(), Provider.KAKAO);
                    return jwtTokenProvider.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
                })
                .orElseGet(() -> { // 고객이 존재하지 않는 경우
                    try{
                        session.setAttribute("kakaoAccount", kakaoAccount); // 세션에 카카오 사용자 정보 저장
                        log.info("KakaoAccount saved in session: {}", session.getAttribute("kakaoAccount"));
                    }catch (Exception e) {
                        log.error("Session 저장 Error : ", e);
                        throw new BaseException(SESSION_SAVE_FAILED);
                    }
                    throw new BaseException(NEED_TO_GIVE_ADDITIONAL_INFORMATION);
                });
    }

    @Transactional
    @Override
    public TokenDTO signInKakaoAdditional(KakaoAdditionalInfoRequestDTO request) {
        KakaoAccountResponseDTO kakaoAccount = (KakaoAccountResponseDTO) session.getAttribute("kakaoAccount");
        if(kakaoAccount == null) {
            throw new BaseException(SESSION_NOT_FOUND);
        }
        /* 데이터 삽입 */
        Customer newCustomer = Customer.of(request, kakaoAccount);
        customerRepository.save(newCustomer);

        UserDetails userDetails = customUserDetailService.loadUserByUsernameAndProvider(
                    kakaoAccount.getKakaoAccount().getEmail(), Provider.KAKAO);

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    @Transactional
    @Override
    public TokenDTO signInNaver(NaverAccountResponseDTO naverAccount) {
        return customerRepository
                .findByEmailAndProvider(naverAccount.getResponse().getEmail(), Provider.NAVER)
                .map(customer -> { // 고객이 존재하는 경우
                    UserDetails userDetails = customUserDetailService
                            .loadUserByUsernameAndProvider(naverAccount.getResponse().getEmail(), Provider.NAVER);
                    return jwtTokenProvider.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
                })
                .orElseGet(() -> { // 고객이 존재하지 않는 경우
                    try{
                        session.setAttribute("naverAccount", naverAccount); // 세션에 카카오 사용자 정보 저장
                        log.info("naverAccount saved in session: {}", session.getAttribute("naverAccount"));
                    }catch (Exception e) {
                        log.error("Session 저장 Error : ", e);
                        throw new BaseException(SESSION_SAVE_FAILED);
                    }
                    throw new BaseException(NEED_TO_GIVE_ADDITIONAL_INFORMATION);
                });
    }

    @Transactional
    @Override
    public TokenDTO signInNaverAdditional(NaverAdditionalInfoRequestDTO request) {
        NaverAccountResponseDTO naverAccount = (NaverAccountResponseDTO) session.getAttribute("naverAccount");
        if(naverAccount == null) {
            throw new BaseException(SESSION_NOT_FOUND);
        }
        /* 데이터 삽입 */
        Customer newCustomer = Customer.of(request, naverAccount);
        customerRepository.save(newCustomer);

        UserDetails userDetails = customUserDetailService.loadUserByUsernameAndProvider(
                naverAccount.getResponse().getEmail(), Provider.NAVER);

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    @Override
    public Customer getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        log.info(user.getUsername(), user.getAuthorities().toString());
        if (!ObjectUtils.isEmpty(user)) {
            String useremail = user.getUsername();
            String authority = user.getAuthorities().stream().findFirst().orElseThrow(
                    () -> new BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
            ).toString();

            log.info("useremail : {} | authority : {}", useremail, authority);

            if (authority.equals(Role.CUSTOMER.getKey())) {
                return customerRepository.findByEmail(useremail).orElseThrow(
                        () -> new BaseException(ErrorCode.SELLER_NOT_FOUND)
                );
            }

            throw new BaseException(ErrorCode.ACCESS_DENIED);
        }

        throw new BaseException(ErrorCode.ACCESS_DENIED);
    }
}
