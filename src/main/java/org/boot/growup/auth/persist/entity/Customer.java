package org.boot.growup.auth.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.auth.model.UserModel;
import org.boot.growup.common.constant.Gender;
import org.boot.growup.common.constant.Role;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.auth.model.dto.response.GoogleAccountResponseDTO;
import org.boot.growup.auth.model.dto.response.KakaoAccountResponseDTO;
import org.boot.growup.auth.model.dto.response.NaverAccountResponseDTO;
import org.boot.growup.auth.model.dto.request.CustomerSignUpRequestDTO;
import org.boot.growup.auth.model.dto.request.Oauth2AdditionalInfoRequestDTO;
import org.boot.growup.common.entity.BaseEntity;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@Table(name = "customer")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Customer extends BaseEntity {
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
    private Gender gender;

    @Column(length = 100)
    private String address;

    @Column(length = 5)
    private String postCode;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 10)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(length = 300)
    private String profileUrl;

    @Column(nullable = false)
    private boolean isValidPhoneNumber;

    @Column(nullable = false)
    private boolean isValidEmail;

    @Column(nullable = false)
    private boolean isAgreeSendEmail;

    @Column(nullable = false)
    private boolean isAgreeSendSms;

    public UserModel toUserDetails() {
        return new UserModel(email, password, role);
    }

    /* 이메일 유저 회원가입 */
    public static Customer of(CustomerSignUpRequestDTO request, String encodedPassword, boolean isValidPhoneNumber) {
        return Customer.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .gender(Gender.valueOf(request.getGender().name()))
                .nickname(request.getNickname())
                .name(request.getName())
                .provider(Provider.EMAIL)
                .role(Role.CUSTOMER)
                .isValidPhoneNumber(isValidPhoneNumber)
                .isValidEmail(request.isValidEmail())
                .isAgreeSendEmail(request.isAgreeSendEmail())
                .isAgreeSendSms(request.isAgreeSendSms())
                .build();
    }

    /* 구글 유저 회원가입 */
    public static Customer of(Oauth2AdditionalInfoRequestDTO request, GoogleAccountResponseDTO googleAccount, boolean isValidPhoneNumber) {
        return Customer.builder()
                .email(googleAccount.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .gender(Gender.valueOf(request.getGender().name()))
                .nickname(request.getNickname())
                .name(request.getName())
                .provider(Provider.GOOGLE)
                .role(Role.CUSTOMER)
                .isValidPhoneNumber(isValidPhoneNumber)
                .isValidEmail(request.isValidEmail())
                .isAgreeSendEmail(request.isAgreeSendEmail())
                .isAgreeSendSms(request.isAgreeSendSms())
                .build();
    }

    /* 카카오 유저 회원가입 */
    public static Customer of(Oauth2AdditionalInfoRequestDTO request, KakaoAccountResponseDTO kakaoAccount, boolean isValidPhoneNumber) {
        return Customer.builder()
                .email(kakaoAccount.getKakaoAccount().getEmail())
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .gender(Gender.valueOf(request.getGender().name()))
                .nickname(request.getNickname())
                .name(request.getName())
                .provider(Provider.KAKAO)
                .role(Role.CUSTOMER)
                .isValidPhoneNumber(isValidPhoneNumber)
                .isValidEmail(request.isValidEmail())
                .isAgreeSendEmail(request.isAgreeSendEmail())
                .isAgreeSendSms(request.isAgreeSendSms())
                .build();
    }

    /* 네이버 유저 회원가입 */
    public static Customer of(Oauth2AdditionalInfoRequestDTO request, NaverAccountResponseDTO naverAccount, boolean isValidPhoneNumber) {
        return Customer.builder()
                .email(naverAccount.getResponse().getEmail())
                .phoneNumber(request.getPhoneNumber())
                .birthday(request.getBirthday())
                .gender(Gender.valueOf(request.getGender().name()))
                .nickname(request.getNickname())
                .name(request.getName())
                .provider(Provider.NAVER)
                .role(Role.CUSTOMER)
                .isValidPhoneNumber(isValidPhoneNumber)
                .isValidEmail(request.isValidEmail())
                .isAgreeSendEmail(request.isAgreeSendEmail())
                .isAgreeSendSms(request.isAgreeSendSms())
                .build();
    }
}
