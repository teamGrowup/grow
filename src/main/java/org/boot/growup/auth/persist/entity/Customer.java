package org.boot.growup.auth.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.constant.Gender;
import org.boot.growup.common.constant.Role;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.auth.model.dto.response.GoogleAccountResponseDTO;
import org.boot.growup.auth.model.dto.response.KakaoAccountResponseDTO;
import org.boot.growup.auth.model.dto.response.NaverAccountResponseDTO;
import org.boot.growup.auth.model.User;
import org.boot.growup.auth.model.dto.request.CustomerSignUpRequestDTO;
import org.boot.growup.auth.model.dto.request.GoogleAdditionalInfoRequestDTO;
import org.boot.growup.auth.model.dto.request.KakaoAdditionalInfoRequestDTO;
import org.boot.growup.auth.model.dto.request.NaverAdditionalInfoRequestDTO;

@Entity
@Getter
@Builder
@Table(name = "customer")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 300)
    private String email;

    @Column(length = 60)
    private String password;

    @Column(nullable = false, length = 13)
    private String phoneNumber;

    @Column(nullable = false, length = 8)
    private String birthday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false, length = 5)
    private String postCode;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 10)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "profile_url", length = 300)
    private String profileUrl;

    @Column(nullable = false)
    private boolean isValidPhoneNumber;

    @Column(nullable = false)
    private boolean isValidEmail;

    @Column(nullable = false)
    private boolean isAgreeSendEmail;

    @Column(nullable = false)
    private boolean isAgreeSendSms;

    public User toUserDetails() {
        return new User(email, password, role);
    }

    /* 이메일 유저 회원가입 */
    public static Customer of(CustomerSignUpRequestDTO request, String encodedPassword) {
        return Customer.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .gender(Gender.valueOf(request.getGender().name()))
                .address(request.getAddress())
                .postCode(request.getPostCode())
                .nickname(request.getNickname())
                .name(request.getName())
                .provider(Provider.EMAIL)
                .role(Role.CUSTOMER)
                .isValidPhoneNumber(request.isValidPhoneNumber())
                .isValidEmail(request.isValidEmail())
                .isAgreeSendEmail(request.isAgreeSendEmail())
                .isAgreeSendSms(request.isAgreeSendSms())
                .build();
    }

    /* 구글 유저 회원가입 */
    public static Customer of(GoogleAdditionalInfoRequestDTO request, GoogleAccountResponseDTO googleAccount) {
        return Customer.builder()
                .email(googleAccount.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .gender(Gender.valueOf(request.getGender().name()))
                .address(request.getAddress())
                .postCode(request.getPostCode())
                .nickname(googleAccount.getGivenName())
                .name(googleAccount.getName())
                .provider(Provider.GOOGLE)
                .role(Role.CUSTOMER)
                .build();
    }

    /* 카카오 유저 회원가입 */
    public static Customer of(KakaoAdditionalInfoRequestDTO request, KakaoAccountResponseDTO kakaoAccount) {
        return Customer.builder()
                .email(kakaoAccount.getKakaoAccount().getEmail())
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .gender(Gender.valueOf(request.getGender().name()))
                .address(request.getAddress())
                .postCode(request.getPostCode())
                .nickname(kakaoAccount.getProperties().get("nickname"))
                .name(request.getName())
                .provider(Provider.KAKAO)
                .role(Role.CUSTOMER)
                .build();
    }

    /* 네이버 유저 회원가입 */
    public static Customer of(NaverAdditionalInfoRequestDTO request, NaverAccountResponseDTO naverAccount) {
        return Customer.builder()
                .email(naverAccount.getResponse().getEmail())
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .gender(Gender.valueOf(naverAccount.getResponse().getGender().equals("M") ? "MALE" : "FEMALE"))
                .address(request.getAddress())
                .postCode(request.getPostCode())
                .nickname(request.getNickname())
                .name(naverAccount.getResponse().getName())
                .provider(Provider.NAVER)
                .role(Role.CUSTOMER)
                .build();
    }
}
