package org.boot.growup.auth.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.auth.model.dto.request.PostAddressRequestDTO;
import org.boot.growup.common.constant.Gender;
import org.boot.growup.common.entity.BaseEntity;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false, length = 5)
    private String postcode;

    @Column(nullable = false, length = 13)
    private String phoneNumber;

    @Column(nullable = false, length = 10)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public static Address of(PostAddressRequestDTO request, Customer customer) {
        return Address.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .postcode(request.getPostcode())
                .customer(customer)
                .build();
    }
}
