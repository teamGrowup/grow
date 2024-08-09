package org.boot.growup.source.seller.service;

import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.dto.response.ProductResponseDTO;
import org.boot.growup.source.seller.persist.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductService {
    ProductResponseDTO registerProduct(ProductRequestDTO productRequestDto, List<MultipartFile> images);
}
