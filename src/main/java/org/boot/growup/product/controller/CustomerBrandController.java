package org.boot.growup.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.product.application.BrandApplication;
import org.boot.growup.product.dto.response.GetBrandDetailResponseDTO;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerBrandController {
    private final BrandApplication brandApplication;

    /**
     * 브랜드 상세 조회 (설명, 이미지)
     * @param brandId
     * @return GetBrandDetailResponseDTO
     */
    @GetMapping("/customers/brands/brand/{brandId}")
    public BaseResponse<GetBrandDetailResponseDTO> getBrandDetail(@PathVariable Long brandId){
        return new BaseResponse<>(brandApplication.getBrandDetail(brandId));
    }

    // 브랜드 랭킹 순위별 조회 *좋아요 순

    // 브랜드 상품 조회
//    @GetMapping("/customers/brands/brand-products/{brandId}")
//    public BaseResponse<?> getBrandProducts(@PathVariable Long brandId){
//        return new BaseResponse<>(brandApplication.getBrandProducts());
//    }

    // 브랜드의 조건별 주문 내역 조회

    // 브랜드의 정산금(구매확정으로 인한 발생한 이익) 조회

    // 브랜드의 조건별(날짜, 상태) 주문 건수 조회


}
