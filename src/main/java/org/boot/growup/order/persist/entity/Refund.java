package org.boot.growup.order.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.RefundStatus;
import org.boot.growup.common.entity.BaseEntity;
import org.boot.growup.order.utils.RefundIdSequence;
import org.hibernate.envers.AuditOverride;

@Slf4j
@Entity
@Getter
@Builder
@Table(name = "refund")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Refund extends BaseEntity {
    @Id @RefundIdSequence
    @Column(name = "refund_id", nullable = false)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @Column(nullable = false, length = 100)
    private String reason;

    @Column(nullable = false)
    private int deliveryFee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus refundStatus;

    @Column(nullable = false)
    private int price;
}
