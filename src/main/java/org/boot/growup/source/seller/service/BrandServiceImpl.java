package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.source.seller.dto.request.RegisterBrandRequestDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    // seller 객체 넘겨주기.
    @Transactional
    public Brand registerBrand(RegisterBrandRequestDTO registerBrandRequestDTO) {

        if (brandRepository.findByName(registerBrandRequestDTO.getName()).isPresent()) {
            throw new BaseException(ErrorCode.BRAND_NAME_ALREADY_EXISTS);
        }

        Brand brand = Brand.from(registerBrandRequestDTO);

        brand.pending();
        brand.initLikesCnt();
        // TODO : brand.designateSeller(seller); 판매자 설정.
        brandRepository.save(brand);

        return brand;
    }
}
