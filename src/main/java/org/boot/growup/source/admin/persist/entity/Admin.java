package org.boot.growup.source.admin.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.enumerate.Role;
import org.boot.growup.common.userdetail.CustomUserDetails;

@Entity
@Getter
@Builder
@Table(name = "admin")
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id", nullable = false)
    private Long id;

    @Column(name = "admin_uid", nullable = false, length = 300)
    private String uid;

    @Column(name = "admin_password", nullable = false, length = 60)
    private String password;

    @Column(nullable = false)
    private int balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public CustomUserDetails toUserDetails() {
        return new CustomUserDetails(uid, password, role);
    }
}
