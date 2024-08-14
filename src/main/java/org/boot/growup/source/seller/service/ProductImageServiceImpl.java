package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.ImageStore;
import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductImage;
import org.boot.growup.source.seller.persist.repository.ProductImageRepository;
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

    private final String productImageDir = "C:\\Users\\xcv41\\IdeaProjects\\grow\\src\\main\\java\\org\\boot\\growup\\source\\seller";

    public String getFullPath(String filename) {
        return productImageDir + filename;
    }

    @Transactional
    public void saveProductImages(List<MultipartFile> productImages, Product product, Section section) {

        for (MultipartFile multipartFile : productImages) {
            if (!multipartFile.isEmpty()) {
                ProductImage uploadImage = storeImage(multipartFile,section); // 이미지 저장 로직
                uploadImage.designateProduct(product); // 상품 설정
                productImageRepository.save(uploadImage); // 이미지 저장
            }
        }
    }

    public ProductImage storeImage(MultipartFile multipartFile, Section section) {
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

        // 로그 출력
        System.out.println("Section value: " + section);

        return ProductImage.builder()
                .originalImageName(originalFilename)
                .path(storeFilename)
                .section(Section.valueOf(section.name()))
                .build(); // section은 설정하지 않음
    }
    @Transactional
    @Override
    public void updateProductImages(List<MultipartFile> productImages, Product product, Section section) {
        // 1. 현재 등록된 상품 이미지를 지움.
        productImageRepository.deleteProductImageByProduct_Id(product.getId());

        // 2. 해당 상품에 이미지를 새로 등록함.
        for (MultipartFile multipartFile : productImages) {
            if (!multipartFile.isEmpty()) {
                ProductImage uploadImage = storeImage(multipartFile,section); // 이미지 저장 로직
                uploadImage.designateProduct(product); // 상품 설정
                productImageRepository.save(uploadImage); // 이미지 저장
            }
        }
    }
}

