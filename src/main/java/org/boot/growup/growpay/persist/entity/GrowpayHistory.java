package org.boot.growup.growpay.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.constant.TransactionStatus;
import org.hibernate.envers.AuditOverride;
import org.boot.growup.common.entity.BaseEntity;

@Entity
@Getter
@Builder
@Table(name = "growpay_history")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class GrowpayHistory extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @Column(nullable = false)
    private int amount; // 거래 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus; // 거래 상태 (예: SUCCESS, FAILED)

    @Column(name = "growpay_id", nullable = false)
    private Long growpayId; // 연결된 GrowPay ID

    public void payment() { this.transactionStatus = TransactionStatus.PAYMENT; }

    public void refund() {
        this.transactionStatus = TransactionStatus.REFUND;
    }
}
