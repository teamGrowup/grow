package org.boot.growup.growpay.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.common.entity.BaseEntity;

@Entity
@Getter
@Builder
@Table(name = "growpay")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Growpay extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "growpay_id")
    private Long id;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "growpay_balance", nullable = false)
    private int growpayBalance; // 잔액

    public void depositBalance(int amount) { // 입금
        this.growpayBalance += amount;
    }

    public void withdrawBalance(int amount) { // 출금
        this.growpayBalance -= amount;
    }
}
