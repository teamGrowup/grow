package org.boot.growup.source.seller.service;

import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.dto.response.ProductResponseDTO;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    Product registerProduct(ProductRequestDTO productRequestDto, Seller seller);
    Product updateProduct(ProductRequestDTO productRequestDto, Seller seller);
}
