package org.boot.growup.source.seller.service;

import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface ProductImageService {
    /**
     * 상품 이미지 파일을 저장합니다.
     *
     * @param productImageFiles 저장할 이미지 파일 목록
     * @param product 저장할 이미지와 연관된 상품
     */
    void saveProductImages(List<MultipartFile> productImageFiles, Product product);

}
