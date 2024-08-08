package org.boot.growup.source.seller.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.source.seller.constant.AuthorityStatus;
import org.boot.growup.source.seller.dto.BrandPostDTO;

@Entity
@Getter
@Builder
@Table(name = "brand")
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private AuthorityStatus authorityStatus;
    private int likes;

    @OneToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    public static Brand from(BrandPostDTO brandPostDTO) {
        return Brand.builder()
                .name(brandPostDTO.getName())
                .description(brandPostDTO.getDescription())
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
        this.likes = 0;
    }
}
