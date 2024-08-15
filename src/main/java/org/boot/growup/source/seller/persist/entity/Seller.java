package org.boot.growup.source.seller.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.enumerate.Role;
import org.boot.growup.common.userdetail.CustomUserDetails;
import org.boot.growup.source.seller.dto.request.SellerSignUpRequestDTO;

@Entity
@Getter
@Builder
@Table(name = "seller")
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 300)
    private String cpEmail;

    @Column(nullable = false, length = 60)
    private String cpPassword;

    @Column(nullable = false, length = 14)
    private String phoneNumber;

    @Column(nullable = false, length = 10)
    private String epName;

    @Column(nullable = false, length = 20)
    private String cpCode;

    @Column(nullable = false, length = 50)
    private String cpName;

    @Column(nullable = false, length = 100)
    private String cpAddress;

    @Column(nullable = true)
    private int netProceeds;

    @OneToOne(mappedBy = "seller")
    private Brand brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public CustomUserDetails toUserDetails() {
        return new CustomUserDetails(cpEmail, cpPassword, role);
    }

    /* 판매자 회원가입 */
    public static Seller of(SellerSignUpRequestDTO request, String encodedPassword) {
        return Seller.builder()
                .cpEmail(request.getCpEmail())
                .cpPassword(encodedPassword)
                .phoneNumber(request.getPhoneNumber())
                .epName(request.getEpName())
                .cpCode(request.getCpCode())
                .cpName(request.getCpName())
                .cpAddress(request.getCpAddress())
                .role(Role.SELLER)
                .build();
    }

}
