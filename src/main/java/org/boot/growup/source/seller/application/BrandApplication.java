package org.boot.growup.source.seller.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.PostBrandRequestDTO;
import org.boot.growup.source.seller.dto.response.GetBrandDetailResponseDTO;
import org.boot.growup.source.seller.dto.response.GetBrandRequestByStatusResponseDTO;
import org.boot.growup.source.seller.dto.response.GetSellerBrandResponseDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.BrandImage;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.boot.growup.source.seller.service.BrandImageService;
import org.boot.growup.source.seller.service.BrandService;
import org.boot.growup.source.seller.service.SellerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandApplication {
    private final BrandService brandService;
    private final BrandImageService brandImageService;
    private final SellerService sellerService;

    /*
    브랜드 및 브랜드 이미지 등록
     */
    @Transactional
    public void postBrandWithBrandImages(PostBrandRequestDTO postBrandRequestDTO, List<MultipartFile> brandImageFiles) {
        Seller seller = sellerService.getCurrentSeller();

        Brand brand = brandService.postBrand(postBrandRequestDTO, seller);
        brandImageService.postBrandImages(brandImageFiles, brand);
    }

    /*
    판매자의 브랜드 조회
     */
    public GetSellerBrandResponseDTO getSellerBrand() {
        Seller seller = sellerService.getCurrentSeller();

        Brand brand = brandService.getBrandBySellerId(seller.getId());
        List<BrandImage> brandImages = brandImageService.getBrandImages(brand.getId());

        return GetSellerBrandResponseDTO.builder()
                .name(brand.getName())
                .description(brand.getDescription())
                .brandImages(
                        brandImages.stream().map(GetSellerBrandResponseDTO.BrandImageDTO::from).toList()
                )
                .build();
    }

    /*
    판매자의 브랜드 및 이미지 수정
     */
    @Transactional
    public void patchBrand(PostBrandRequestDTO postBrandRequestDTO, List<MultipartFile> brandImageFiles) {
        Seller seller = sellerService.getCurrentSeller();

        Brand brand = brandService.patchBrand(postBrandRequestDTO, seller);
        brandImageService.patchBrandImages(brandImageFiles, brand);
    }

    /*
    브랜드등록 거부
     */
    @Transactional
    public void denyBrandPost(Long brandId){
        brandService.changeBrandAuthority(brandId, AuthorityStatus.DENIED);

        // TODO : 1. 해당 브랜드가 판매하는 모든 등록된 상품들 deny
    }

    /*
    브랜드등록 허가
     */
    @Transactional
    public void approveBrandPost(Long brandId) {
        // 브랜드 승인될 시
        brandService.changeBrandAuthority(brandId, AuthorityStatus.APPROVED);
    }

    /*
    브랜드등록 허가대기중
     */
    @Transactional
    public void pendingBrandRegister(Long brandId) {
        // 브랜드 허가대기중 상태로 임의로 변경
        brandService.changeBrandAuthority(brandId, AuthorityStatus.PENDING);
    }

    /*
    상태별 브랜드 등록 확인
     */
    public List<GetBrandRequestByStatusResponseDTO> getBrandRequestByStatus(AuthorityStatus authorityStatus, int pageNo) {
        List<Brand> brandList = brandService.getBrandRequestsByStatus(authorityStatus, pageNo);
        return brandList.stream()
                .map(GetBrandRequestByStatusResponseDTO::from)
                .toList();
    }

    /*
    브랜드 정보 가져오기
     */
    public GetBrandDetailResponseDTO getBrandDetail(Long brandId) {
        Brand brand = brandService.getBrandById(brandId);
        List<BrandImage> brandImages = brandImageService.getBrandImages(brand.getId());
        return GetBrandDetailResponseDTO.of(brand, brandImages);
    }
}
