package org.boot.growup.source.seller.service;

import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    /*
    상품 이미지 등록
     */
    void postProductImages(List<MultipartFile> productImageFiles, Product product, Section section);

    /*
    상품 이미지 읽어오기
     */
    List<ProductImage> getProductImages(Long id);
    
    /*
    등록된 상품 이미지 수정
     */
    void patchProductImages(List<MultipartFile> productImages, Product product, Section section);
}
