package org.boot.growup.source.seller.service;

import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.BrandImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface BrandImageService {
    void saveBrandImages(List<MultipartFile> brandImageFiles, Brand brand);
    List<BrandImage> readBrandImages(Long id);
    void updateBrandImages(List<MultipartFile> brandImageFiles, Brand brand);
}
