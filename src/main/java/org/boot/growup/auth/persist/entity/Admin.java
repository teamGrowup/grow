package org.boot.growup.auth.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.common.constant.Role;
import org.boot.growup.auth.model.UserModel;
import org.boot.growup.common.entity.BaseEntity;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@Table(name = "admin")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Admin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id", nullable = false)
    private Long id;

    @Column(name = "admin_email", nullable = false, length = 300)
    private String email;

    @Column(name = "admin_password", nullable = false, length = 60)
    private String password;

    @Column(nullable = false)
    private int balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public UserModel toUserDetails() {
        return new UserModel(email, password, role);
    }
}
