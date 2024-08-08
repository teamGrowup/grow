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
    private Long id;

    private String cpEmail;
    private String cpPassword;
    private String phoneNumber;
    private String epName;
    private String cpCode;
    private String cpName;
    private String cpAddress;
    private int netProceeds;

    @OneToOne(mappedBy = "seller")
    private Brand brand;

}
