package org.boot.growup.source.seller.service;

import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product registerProduct(ProductRequestDTO productRequestDto, Seller seller);
    Product updateProduct(ProductRequestDTO productRequestDto, Seller seller, Long productId);
    void changeProductAuthority(Long productId, AuthorityStatus status);
    List<Product> readProductRequestsByStatus(AuthorityStatus authorityStatus, int pageNo);
}
