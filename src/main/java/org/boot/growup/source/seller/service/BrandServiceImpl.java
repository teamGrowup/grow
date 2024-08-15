package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.source.seller.dto.request.PostBrandRequestDTO;
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
    public Brand postBrand(PostBrandRequestDTO postBrandRequestDTO, Seller seller) {

        if (brandRepository.findByName(postBrandRequestDTO.getName()).isPresent()) {
            throw new BaseException(ErrorCode.BRAND_NAME_ALREADY_EXISTS);
        }

        Brand brand = Brand.from(postBrandRequestDTO);

        brand.pending();
        brand.initLikesCnt();
        brand.designateSeller(seller); // 판매자 설정.
        brandRepository.save(brand);

        return brand;
    }

    @Override
    public Brand getBrandBySellerId(Long sellerId) {
        return brandRepository.findBySeller_Id(sellerId).orElseThrow(
                () -> new BaseException(ErrorCode.BRAND_BY_SELLER_NOT_FOUND)
        );
    }

    @Transactional
    @Override
    public Brand patchBrand(PostBrandRequestDTO postBrandRequestDTO, Seller seller) {
        Brand brand = brandRepository.findBySeller_Id(seller.getId()).orElseThrow(
                () -> new BaseException(ErrorCode.BRAND_BY_SELLER_NOT_FOUND)
        );

        brand.pending(); // 대기 상태로 변경.
        brand.updateBrandInfo(postBrandRequestDTO.getName(), postBrandRequestDTO.getDescription());

        return brand;
    }

    @Transactional
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
    public List<Brand> getBrandRequestsByStatus(AuthorityStatus authorityStatus, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);

        if (ObjectUtils.isEmpty(authorityStatus)){
            return brandRepository.findAll(pageable).stream().toList();
        }

        return brandRepository.findByAuthorityStatus(authorityStatus, pageable);
    }

    @Override
    public Brand getBrandById(Long brandId) {
        return brandRepository.findById(brandId).orElseThrow(
                () -> new BaseException(ErrorCode.BRAND_BY_ID_NOT_FOUND)
        );
    }
}
