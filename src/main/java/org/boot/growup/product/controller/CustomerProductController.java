package org.boot.growup.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.product.application.ProductApplication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customers/products")
@RequiredArgsConstructor
public class CustomerProductController {
    private final ProductApplication productApplication;

    /**
     * [POST]
     * 상품 좋아요
     * @header Customer's AccessToken
     * @param productId 상품 ID
     * @response String
     */
    @PostMapping("/{productId}/like")
    public BaseResponse<String> postLikeProduct(@PathVariable Long productId) {
        productApplication.postProductLike(productId);
        return new BaseResponse<>("상품의 좋아요가 완료되었습니다.");
    }

    /**
     * [DELETE]
     * 상품 좋아요 취소
     * @header Customer's AccessToken
     * @param productId 상품 ID
     * @response String
     */
    @DeleteMapping("/{productId}/like")
    public BaseResponse<String> deleteLikeProduct(@PathVariable Long productId) {
        productApplication.deleteProductLike(productId);
        return new BaseResponse<>("상품의 좋아요가 취소되었습니다.");
    }
}
