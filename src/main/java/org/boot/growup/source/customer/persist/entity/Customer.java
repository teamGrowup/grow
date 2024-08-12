package org.boot.growup.source.customer.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.enumerate.Gender;
import org.boot.growup.common.enumerate.Role;
import org.boot.growup.common.oauth2.Provider;
import org.boot.growup.common.oauth2.google.dto.GoogleAccountResponseDTO;
import org.boot.growup.common.userdetail.CustomUserDetails;
import org.boot.growup.source.customer.dto.request.CustomerSignUpRequestDTO;
import org.boot.growup.source.customer.dto.request.GoogleAdditionalInfoRequestDTO;
import org.hibernate.validator.constraints.br.CPF;

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

    private String profileUrl;

    public CustomUserDetails toUserDetails() {
        return new CustomUserDetails(email, password, role);
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
}
