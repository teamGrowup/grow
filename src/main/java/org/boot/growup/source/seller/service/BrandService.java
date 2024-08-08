package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.source.seller.dto.BrandPostDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {
    private final BrandRepository brandRepository;

    // seller 객체 넘겨주기.
    @Transactional
    public Brand registerBrand(BrandPostDTO brandPostDTO) {

        if (brandRepository.findByName(brandPostDTO.getName()).isPresent()) {
            throw new IllegalStateException("해당 브랜드명은 이미 존재합니다.");
        }

        Brand brand = Brand.from(brandPostDTO);
        brand.pending();
        brand.initLikesCnt();
        // TODO : brand.designateSeller(seller); 판매자 설정.
        brandRepository.save(brand);

        return brand;
    }
}
