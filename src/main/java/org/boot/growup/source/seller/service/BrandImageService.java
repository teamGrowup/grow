package org.boot.growup.source.seller.service;

import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.BrandImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface BrandImageService {
    /*
    브랜드 이미지 저장
     */
    void postBrandImages(List<MultipartFile> brandImageFiles, Brand brand);

    /*
    브랜드 이미지 읽어오기
     */
    List<BrandImage> getBrandImages(Long id);

    /*
    브랜드 이미지 수정하기
     */
    void patchBrandImages(List<MultipartFile> brandImageFiles, Brand brand);
}
