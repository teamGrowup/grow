package org.boot.growup.source.seller.service;

import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    void saveProductImages(List<MultipartFile> productImageFiles, Product product, ProductImage.Section section);
    ProductImage storeImage(MultipartFile multipartFile, ProductImage.Section section);
    String getFullPath(String filename);
    void updateProductImages(List<MultipartFile> productImages, Product product, ProductImage.Section section);
}
