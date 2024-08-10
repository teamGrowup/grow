package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.ImageStore;
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

    private String productImageDir = "C:\\Users\\xcv41\\IdeaProjects\\grow\\src\\main\\java\\org\\boot\\growup\\source\\seller";

    public String getFullPath(String filename) {
        return productImageDir + filename;
    }

    @Transactional
    public void saveProductImages(List<MultipartFile> productImageFiles, Product product) {
        int sectionIndex = 1; // 섹션 인덱스 초기화

        for (MultipartFile multipartFile : productImageFiles) {
            if (!multipartFile.isEmpty()) {
                // 섹션 값 결정
                ProductImage.Section section = (sectionIndex == 1)
                        ? ProductImage.Section.PRODUCT_IMAGE
                        : ProductImage.Section.PRODUCT_DETAIL_IMAGE;


                // 로그 출력
                System.out.println("Saving image with section: " + section);
                // 이미지 저장 및 섹션 설정
                ProductImage uploadImage = storeImage(multipartFile, section);
                uploadImage.designateProduct(product);

                productImageRepository.save(uploadImage); // 데이터베이스에 저장
                sectionIndex++; // 섹션 인덱스 증가
            }
        }
    }

    public ProductImage storeImage(MultipartFile multipartFile, ProductImage.Section section) {
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
                .section(section)
                .build(); // section은 설정하지 않음
    }
}

