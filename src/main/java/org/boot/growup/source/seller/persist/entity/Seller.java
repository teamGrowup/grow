package org.boot.growup.source.seller.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false, length = 11)
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

}
