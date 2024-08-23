package org.boot.growup.auth.service.impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.boot.growup.auth.model.UserModel;
import org.boot.growup.auth.model.dto.request.*;
import org.boot.growup.auth.service.CustomerService;
import org.boot.growup.common.model.EmailMessageDTO;
import org.boot.growup.common.constant.Role;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.auth.utils.JwtTokenProvider;
import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.auth.model.dto.response.GoogleAccountResponseDTO;
import org.boot.growup.auth.model.dto.response.KakaoAccountResponseDTO;
import org.boot.growup.auth.model.dto.response.NaverAccountResponseDTO;
import org.boot.growup.common.model.RedisDAO;
import org.boot.growup.auth.utils.SmsUtil;
import org.boot.growup.auth.model.dto.response.EmailCheckResponseDTO;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.persist.repository.CustomerRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

import org.springframework.util.ObjectUtils;

import static org.boot.growup.common.constant.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailServiceImpl emailServiceImpl;
    private final HttpSession session;
    private final SmsUtil smsUtil;
    private final RedisDAO redisDao;

    @Override
    public void signUp(CustomerSignUpRequestDTO request) {
        /* 전화번호 인증정보 참조 */
        boolean isValidPhoneNumber = Boolean.parseBoolean(redisDao.getValues(request.getPhoneNumber()));
        log.info("isValidPhoneNumber : {}", isValidPhoneNumber);
        if(!isValidPhoneNumber) {
            throw new BaseException(INVALID_PHONE_NUMBER);
        }

        /* 비밀번호 암호화 */
        String encodedPassword = encodingPassword(request);
        log.info("SignUp Method => before pw : {} | after store pw : {}"
                , request.getPassword()
                , encodedPassword);

        /* 데이터 삽입 */
        Customer newCustomer = Customer.of(request, encodedPassword, isValidPhoneNumber);
        customerRepository.save(newCustomer);

        redisDao.deleteValues(request.getPhoneNumber());
    }

    public String encodingPassword(CustomerSignUpRequestDTO request){
        return passwordEncoder.encode(request.getPassword());
    }

    @Override
    public void postAuthCode(PostAuthCodeRequestDTO request) {
        String storedAuthCode = redisDao.getValues(request.getPhoneNumber());
        if(!storedAuthCode.equals(request.getAuthCode())) {
            throw new BaseException(ErrorCode.PHONE_WRONG_AUTH_CODE);
        }
        redisDao.setValues(request.getPhoneNumber(), String.valueOf(true), 1000 * 60 * 30L);
    }

    @Override
    public void postPhoneNumber(PostPhoneNumberRequestDTO request) {
        /* 이미 가입된 유저인지 확인 */
        customerRepository.findByPhoneNumberAndIsValidPhoneNumberAndProvider(
                        request.getPhoneNumber(), true, Provider.valueOf(request.getProvider().name()))
                .ifPresent(customer -> {
                    /* 이미 가입된 유저라면 가입된 이메일을 반환 */
                    throw new BaseException(USER_ALREADY_REGISTERED, customer.getEmail());
                });

        String parsedPhoneNumber = request.getPhoneNumber().replaceAll("-","");
        String authCode = createAuthCode();
        smsUtil.sendMessage(parsedPhoneNumber, authCode);
        redisDao.setValues(request.getPhoneNumber(), authCode, 1000 * 60 * 5L);
    }

    @Override
    public void deletePhoneNumber(PostPhoneNumberRequestDTO request) {
        redisDao.deleteValues(request.getPhoneNumber());
    }

    @Override
    public TokenDTO signIn(CustomerSignInRequestDTO request) {
        UserDetails userDetails = loadUserByUsernameAndProvider(request.getEmail(), Provider.EMAIL);

        if(!checkPassword(request.getPassword(), userDetails.getPassword())){ // 비밀번호 비교
            throw new BaseException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public EmailCheckResponseDTO checkEmail(EmailCheckRequestDTO request) throws MessagingException {
        EmailMessageDTO emailMessage = EmailMessageDTO.from(request);
        String validationCode = emailServiceImpl.sendMail(emailMessage);
        return EmailCheckResponseDTO.builder()
                .validationCode(validationCode)
                .build();
    }

    @Override
    public TokenDTO signInGoogle(GoogleAccountResponseDTO googleAccount) {
        return customerRepository
                .findByEmailAndProvider(googleAccount.getEmail(), Provider.GOOGLE)
                .map(customer -> { // 고객이 존재하는 경우
                    UserDetails userDetails = loadUserByUsernameAndProvider(
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

    @Override
    public TokenDTO signInGoogleAdditional(Oauth2AdditionalInfoRequestDTO request) {
        GoogleAccountResponseDTO googleAccount = (GoogleAccountResponseDTO) session.getAttribute("googleAccount");
        if(googleAccount == null) {
            throw new BaseException(SESSION_NOT_FOUND);
        }

        /* 전화번호 인증정보 참조 */
        boolean isValidPhoneNumber = Boolean.parseBoolean(redisDao.getValues(request.getPhoneNumber()));
        log.info("isValidPhoneNumber : {}", isValidPhoneNumber);
        if(!isValidPhoneNumber) {
            throw new BaseException(INVALID_PHONE_NUMBER);
        }

        /* 데이터 삽입 */
        Customer newCustomer = Customer.of(request, googleAccount, isValidPhoneNumber);
        customerRepository.save(newCustomer);

        UserDetails userDetails = loadUserByUsernameAndProvider(
                    googleAccount.getEmail(), Provider.GOOGLE);

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    @Override
    public TokenDTO signInKakao(KakaoAccountResponseDTO kakaoAccount) {
        return customerRepository
                .findByEmailAndProvider(kakaoAccount.getKakaoAccount().getEmail(), Provider.KAKAO)
                .map(customer -> { // 고객이 존재하는 경우
                    UserDetails userDetails = loadUserByUsernameAndProvider(
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

    @Override
    public TokenDTO signInKakaoAdditional(Oauth2AdditionalInfoRequestDTO request) {
        KakaoAccountResponseDTO kakaoAccount = (KakaoAccountResponseDTO) session.getAttribute("kakaoAccount");
        if(kakaoAccount == null) {
            throw new BaseException(SESSION_NOT_FOUND);
        }

        /* 전화번호 인증정보 참조 */
        boolean isValidPhoneNumber = Boolean.parseBoolean(redisDao.getValues(request.getPhoneNumber()));
        log.info("isValidPhoneNumber : {}", isValidPhoneNumber);
        if(!isValidPhoneNumber) {
            throw new BaseException(INVALID_PHONE_NUMBER);
        }

        /* 데이터 삽입 */
        Customer newCustomer = Customer.of(request, kakaoAccount, isValidPhoneNumber);
        customerRepository.save(newCustomer);

        UserDetails userDetails = loadUserByUsernameAndProvider(
                    kakaoAccount.getKakaoAccount().getEmail(), Provider.KAKAO);

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    @Override
    public TokenDTO signInNaver(NaverAccountResponseDTO naverAccount) {
        return customerRepository
                .findByEmailAndProvider(naverAccount.getResponse().getEmail(), Provider.NAVER)
                .map(customer -> { // 고객이 존재하는 경우
                    UserDetails userDetails = loadUserByUsernameAndProvider(naverAccount.getResponse().getEmail(), Provider.NAVER);
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

    @Override
    public TokenDTO signInNaverAdditional(Oauth2AdditionalInfoRequestDTO request) {
        NaverAccountResponseDTO naverAccount = (NaverAccountResponseDTO) session.getAttribute("naverAccount");
        if(naverAccount == null) {
            throw new BaseException(SESSION_NOT_FOUND);
        }

        /* 전화번호 인증정보 참조 */
        boolean isValidPhoneNumber = Boolean.parseBoolean(redisDao.getValues(request.getPhoneNumber()));
        log.info("isValidPhoneNumber : {}", isValidPhoneNumber);
        if(!isValidPhoneNumber) {
            throw new BaseException(INVALID_PHONE_NUMBER);
        }

        /* 데이터 삽입 */
        Customer newCustomer = Customer.of(request, naverAccount, isValidPhoneNumber);
        customerRepository.save(newCustomer);

        UserDetails userDetails = loadUserByUsernameAndProvider(
                naverAccount.getResponse().getEmail(), Provider.NAVER);

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    private String createAuthCode() {
        SecureRandom random = new SecureRandom();
        int authCode = random.nextInt(900000) + 100000; // 100000 ~ 999999 범위의 숫자 생성
        return String.valueOf(authCode);
    }

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
                        () -> new BaseException(CUSTOMER_NOT_FOUND)
                );
            }

            throw new BaseException(ErrorCode.ACCESS_DENIED);
        }

        throw new BaseException(ErrorCode.ACCESS_DENIED);
    }

    @Override
    public UserDetails loadUserByUsernameAndProvider(String username, Provider provider) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmailAndProvider(username, provider)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        UserModel userDetails = customer.toUserDetails();
        log.info("구매자 권한: {}", userDetails.getAuthorities());
        return userDetails;
    }
}
