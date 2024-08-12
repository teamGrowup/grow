package org.boot.growup.source.seller.service;

import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    void saveProductImages(List<MultipartFile> productImageFiles, Product product, Section section);
    ProductImage storeImage(MultipartFile multipartFile, Section section);
    String getFullPath(String filename);
    void updateProductImages(List<MultipartFile> productImages, Product product, Section section);
}
