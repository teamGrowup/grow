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
    private TransactionStatus transactionStatus; // 입출금 상태 (예: DEPOSIT, WITHDRAW)

    @ManyToOne // Many to One 관계 설정
    @JoinColumn(name = "growpay_id", nullable = false) // 외래 키 설정
    private Growpay growpay; // 연결된 GrowPay 엔티티
}
