package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.source.seller.ImageStore;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductImage;
import org.boot.growup.source.seller.persist.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ImageStore imageStore;

//    @Value("${file.dir.product}")
    private String productImageDir = "/Users/gnues/Documents/grow/Images/productImages/";

    public String getFullPath(String filename) {
        return productImageDir + filename;
    }

    @Transactional
    public void saveProductImages(List<MultipartFile> productImageFiles, Product product) {
        for (MultipartFile multipartFile : productImageFiles) {
            if (!multipartFile.isEmpty()) {
                ProductImage uploadImage = storeImage(multipartFile);
                uploadImage.designateProduct(product);
                productImageRepository.save(uploadImage);
            }
        }
    }

    public ProductImage storeImage(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalStateException("이미지가 없습니다.");
        }

        String originalFilename = multipartFile.getOriginalFilename(); // 원래 이름
        String storeFilename = imageStore.createStoreFileName(originalFilename); // 저장된 이름

        try {
            multipartFile.transferTo(new File(getFullPath(storeFilename))); // 디렉토리에 파일 저장
        } catch (Exception e) {
            throw new IllegalStateException("파일 전송 실패", e);
        }

        return ProductImage.builder()
                .originalImageName(originalFilename)
                .path(storeFilename)
                .build();
    }
}
