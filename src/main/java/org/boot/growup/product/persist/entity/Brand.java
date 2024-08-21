package org.boot.growup.product.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.product.dto.request.PostBrandRequestDTO;

@Entity
@Getter
@Builder
@Table(name = "brand")
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 25)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "authority_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorityStatus authorityStatus;

    @Column(nullable = false)
    private int likeCount;

    @OneToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    public static Brand from(PostBrandRequestDTO postBrandRequestDTO) {
        return Brand.builder()
                .name(postBrandRequestDTO.getName())
                .description(postBrandRequestDTO.getDescription())
                .build();
    }

    /*
    허가 상태 변경 (authorityStatus)
     */

    public void approve(){
        this.authorityStatus = AuthorityStatus.APPROVED;
    }

    public void deny(){
        this.authorityStatus = AuthorityStatus.DENIED;
    }

    public void pending(){
        this.authorityStatus = AuthorityStatus.PENDING;
    }

    /*
    판매자(대표자) 설정
     */

    public void designateSeller(Seller seller){
        this.seller = seller;
    }

    /*
    좋아요 수 설정
     */

    public void initLikesCnt(){
        this.likeCount = 0;
    }

    /*
    brand명 및 상세 설명 수정
     */
    public void updateBrandInfo(String name, String description){
        this.name = name;
        this.description = description;
    }
}
