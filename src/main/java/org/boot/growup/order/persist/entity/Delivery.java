package org.boot.growup.order.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.entity.BaseEntity;
import org.boot.growup.order.dto.request.PatchShipmentRequestDTO;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@Table(name = "delivery")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Delivery extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String trackingNumber;

    @Column(nullable = false)
    private int carrierCode;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private OrderItem orderItem;

    public static Delivery of(PatchShipmentRequestDTO patchShipmentRequestDTO, OrderItem orderItem) {
        return Delivery.builder()
                .trackingNumber(patchShipmentRequestDTO.getTrackingNumber())
                .carrierCode(patchShipmentRequestDTO.getCarrierCode())
                .orderItem(orderItem)
                .build();
    }
}
