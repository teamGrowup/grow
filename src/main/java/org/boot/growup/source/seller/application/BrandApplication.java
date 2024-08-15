package org.boot.growup.source.seller.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.RegisterBrandRequestDTO;
import org.boot.growup.source.seller.dto.response.GetBrandDetailResponseDTO;
import org.boot.growup.source.seller.dto.response.ReadBrandRequestByStatusResponseDTO;
import org.boot.growup.source.seller.dto.response.ReadSellerBrandResponseDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.BrandImage;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.boot.growup.source.seller.persist.repository.SellerRepository;
import org.boot.growup.source.seller.service.BrandImageService;
import org.boot.growup.source.seller.service.BrandService;
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
    private final SellerRepository sellerRepository;

    /*
        브랜드 및 브랜드 이미지 등록
     */
    @Transactional
    public void registerBrandWithBrandImages(RegisterBrandRequestDTO registerBrandRequestDTO, List<MultipartFile> brandImageFiles) {
        // TODO : 현재 유저가 seller인지 확인 및 seller 가져오기.

        // TODO : 해당 seller가 brand를 갖고 있는지 검사.
        Seller seller = sellerRepository.findById(1L).get();

        Brand brand = brandService.registerBrand(registerBrandRequestDTO, seller);
        brandImageService.saveBrandImages(brandImageFiles, brand);
    }

    /*
        판매자의 브랜드 조회
     */
    public ReadSellerBrandResponseDTO readSellerBrand() {
        // TODO : 현재 유저가 seller인지 확인 및 seller 가져오기

        // TODO : 해당 seller가 brand를 갖고 있는지 검사.
        Long sellerId = 1L;
        Brand brand = brandService.readBrandBySellerId(sellerId);
        List<BrandImage> brandImages = brandImageService.readBrandImages(brand.getId());

        return ReadSellerBrandResponseDTO.builder()
                .name(brand.getName())
                .description(brand.getDescription())
                .brandImages(
                        brandImages.stream().map(ReadSellerBrandResponseDTO.BrandImageDTO::from).toList()
                )
                .build();
    }

    /*
        판매자의 브랜드 및 이미지 수정
     */
    @Transactional
    public void updateBrand(RegisterBrandRequestDTO registerBrandRequestDTO, List<MultipartFile> brandImageFiles) {
        // TODO : 현재 유저가 seller인지 확인 및 seller 가져오기.

        // TODO : 해당 seller가 brand를 갖고 있는지 검사.
        Seller seller = sellerRepository.findById(1L).get();

        Brand brand = brandService.updateBrand(registerBrandRequestDTO, seller);
        brandImageService.updateBrandImages(brandImageFiles, brand);
    }


    @Transactional
    public void denyBrandRegister(Long brandId){
        // 브랜드가 거부될 시.
        brandService.changeBrandAuthority(brandId, AuthorityStatus.DENIED);

        // TODO : 1. 해당 브랜드가 판매하는 모든 등록된 상품들 deny
    }

    @Transactional
    public void approveBrandRegister(Long brandId) {
        // 브랜드 승인될 시
        brandService.changeBrandAuthority(brandId, AuthorityStatus.APPROVED);
    }

    @Transactional
    public void pendingBrandRegister(Long brandId) {
        // 브랜드 허가대기중 상태로 임의로 변경
        brandService.changeBrandAuthority(brandId, AuthorityStatus.PENDING);
    }

    public List<ReadBrandRequestByStatusResponseDTO> readBrandRequestByStatus(AuthorityStatus authorityStatus, int pageNo) {
        List<Brand> brandList = brandService.readBrandRequestsByStatus(authorityStatus, pageNo);
        return brandList.stream()
                .map(ReadBrandRequestByStatusResponseDTO::from)
                .toList();
    }

    public GetBrandDetailResponseDTO getBrandDetail(Long brandId) {
        Brand brand = brandService.getBrandById(brandId);
        List<BrandImage> brandImages = brandImageService.readBrandImages(brand.getId());
        return GetBrandDetailResponseDTO.of(brand, brandImages);
    }
}
