package org.boot.growup.product.service.Impl;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.utils.ImageStore;
import org.boot.growup.common.utils.S3Service;
import org.boot.growup.product.dto.request.PostBrandRequestDTO;
import org.boot.growup.product.persist.entity.Brand;
import org.boot.growup.product.persist.entity.BrandImage;
import org.boot.growup.product.persist.repository.BrandImageRepository;
import org.boot.growup.product.persist.repository.BrandRepository;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.product.service.BrandService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BrandImageRepository brandImageRepository;
    private final ImageStore imageStore;
    private final S3Service s3Service;

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

        if (ObjectUtils.isEmpty(authorityStatus)) {
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

    public void patchBrandImages(List<MultipartFile> brandImageFiles, Brand brand) {
        // 1. 현재 S3에 등록된 브랜드 이미지를 지움.
        brandImageRepository.findBrandImageByBrand_Id(brand.getId()).forEach(m -> s3Service.deleteFile(m.getPath()));

        // 2. DB에 있는 브랜드 이미지 삭제.
        brandImageRepository.deleteBrandImageByBrand_Id(brand.getId());

        // 3. 해당 브랜드에 이미지를 새로 등록함.
        for (MultipartFile multipartFile : brandImageFiles) {
            if (!multipartFile.isEmpty()) {
                BrandImage uploadImage = storeImage(multipartFile);
                uploadImage.designateBrand(brand);
                brandImageRepository.save(uploadImage);
            }
        }
    }
    public void postBrandImages(List<MultipartFile> brandImageFiles, Brand brand) {
        for (MultipartFile multipartFile : brandImageFiles) {
            if (!multipartFile.isEmpty()) {
                BrandImage uploadImage = storeImage(multipartFile);
                uploadImage.designateBrand(brand);
                brandImageRepository.save(uploadImage);
            }
        }
    }

    @Override
    public List<BrandImage> getBrandImages(Long id) {
        return brandImageRepository.findBrandImageByBrand_Id(id);
    }

    private BrandImage storeImage(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalStateException("이미지가 없습니다.");
        }

        String originalFilename = multipartFile.getOriginalFilename(); // 원래 이름
        String storeFilename = imageStore.createStoreFileName(originalFilename); // 저장된 이름
        String path;
        try {
            path = s3Service.uploadFileAndGetUrl(multipartFile, storeFilename);
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 오류 발생", e);
        }

        return BrandImage.builder()
                .originalImageName(originalFilename)
                .path(path)
                .build();
    }
}
