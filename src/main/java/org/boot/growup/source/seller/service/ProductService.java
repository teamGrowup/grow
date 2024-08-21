package org.boot.growup.source.seller.service;

import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.seller.dto.request.PostProductRequestDTO;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.Seller;

import java.util.List;

public interface ProductService {
    /*
    상품 등록
     */
    Product postProduct(PostProductRequestDTO postProductRequestDto, Seller seller);

    /*
        SellerId로 Product 정보 가져오기
    */
    Product getProductBySellerId(Long sellerId);

    /*
    등록된 상품 수정
     */
    Product patchProduct(PostProductRequestDTO postProductRequestDto, Seller seller, Long productId);

    /*
    등록된 상품의 권한 변경 > 허가, 대기, 거부
     */
    void changeProductAuthority(Long productId, AuthorityStatus status);

    /*
    권한 상태에 따른 상품 검색
     */
    List<Product> getProductRequestsByStatus(AuthorityStatus authorityStatus, int pageNo);

    /*
    상품 좋아요 증가
     */
    void postProductLike(Long productId, Customer customer);

    /*
    상품 좋아요 감소
     */
    void deleteProductLike(Long productId, Customer customer);
}
