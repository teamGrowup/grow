package org.boot.growup.source.seller.service;

import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    /*
    상품 등록
     */
    Product registerProduct(ProductRequestDTO productRequestDto, Seller seller);
    /*
    등록된 상품 수정
     */
    Product updateProduct(ProductRequestDTO productRequestDto, Seller seller, Long productId);
    /*
    등록된 상품의 권한 변경 > 허가, 대기, 거부
     */
    void changeProductAuthority(Long productId, AuthorityStatus status);
    /*
    권한 상태에 따른 상품 검색
     */
    List<Product> readProductRequestsByStatus(AuthorityStatus authorityStatus, int pageNo);
}
