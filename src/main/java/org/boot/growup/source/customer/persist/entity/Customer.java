package org.boot.growup.source.customer.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.enumerate.Gender;
import org.boot.growup.common.enumerate.Role;
import org.boot.growup.common.oauth2.Provider;
import org.boot.growup.common.userdetail.CustomUserDetails;
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
}
