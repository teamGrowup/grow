package org.boot.growup.source.seller.service;

import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.persist.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    /*
    상품 이미지 등록
     */
    void saveProductImages(List<MultipartFile> productImageFiles, Product product, Section section);
    /*
    등록된 상품 이미지 수정
     */
    void updateProductImages(List<MultipartFile> productImages, Product product, Section section);
}
