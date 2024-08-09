package org.boot.growup.source.seller.application;

import lombok.RequiredArgsConstructor;
import org.boot.growup.source.seller.dto.request.RegisterBrandRequestDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.service.BrandImageService;
import org.boot.growup.source.seller.service.BrandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandApplication {
    private final BrandService brandService;
    private final BrandImageService brandImageService;


    @Transactional
    public void registerBrandWithBrandImages(RegisterBrandRequestDTO registerBrandRequestDTO, List<MultipartFile> brandImageFiles) {
        // TODO : 현재 유저가 seller인지 확인 및 seller 가져오기.

        // TODO : 해당 seller가 brand를 갖고 있는지 검사.

        Brand brand = brandService.registerBrand(registerBrandRequestDTO);
        brandImageService.saveBrandImages(brandImageFiles, brand);
    }
}
