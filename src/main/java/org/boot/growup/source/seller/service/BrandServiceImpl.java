package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.error.BaseException;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.source.seller.dto.request.RegisterBrandRequestDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.boot.growup.source.seller.persist.repository.BrandRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    // seller 객체 넘겨주기.
    @Transactional
    @Override
    public Brand registerBrand(RegisterBrandRequestDTO registerBrandRequestDTO, Seller seller) {

        if (brandRepository.findByName(registerBrandRequestDTO.getName()).isPresent()) {
            throw new BaseException(ErrorCode.BRAND_NAME_ALREADY_EXISTS);
        }

        Brand brand = Brand.from(registerBrandRequestDTO);

        brand.pending();
        brand.initLikesCnt();
        brand.designateSeller(seller); // 판매자 설정.
        brandRepository.save(brand);

        return brand;
    }

    @Override
    public Brand readBrandBySellerId(Long sellerId) {
        return brandRepository.findBySeller_Id(sellerId).orElseThrow(
                () -> new BaseException(ErrorCode.BRAND_BY_SELLER_NOT_FOUND)
        );
    }

    @Transactional
    @Override
    public Brand updateBrand(RegisterBrandRequestDTO registerBrandRequestDTO, Seller seller) {
        Brand brand = brandRepository.findBySeller_Id(seller.getId()).orElseThrow(
                () -> new BaseException(ErrorCode.BRAND_BY_SELLER_NOT_FOUND)
        );

        brand.pending(); // 대기 상태로 변경.
        brand.updateBrandInfo(registerBrandRequestDTO.getName(), registerBrandRequestDTO.getDescription());

        return brand;
    }

    @Override
    public void changeBrandAuthority(Long brandId, AuthorityStatus status) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(
                () -> new BaseException(ErrorCode.BRAND_BY_SELLER_NOT_FOUND)
        );

        switch (status) {
            case DENIED -> brand.deny();
            case PENDING -> brand.pending();
            case APPROVED -> brand.approve();
        }
    }

    @Override
    public List<Brand> readBrandRequestsByStatus(AuthorityStatus authorityStatus, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);

        if (ObjectUtils.isEmpty(authorityStatus)){
            return brandRepository.findAll(pageable).stream().toList();
        }

        return brandRepository.findByAuthorityStatus(authorityStatus, pageable);
    }
}
