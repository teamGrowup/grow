package org.boot.growup.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.product.application.BrandApplication;
import org.boot.growup.product.dto.response.GetBrandRequestByStatusResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admins/brand-requests")
@RequiredArgsConstructor
public class AdminBrandController {
    private final BrandApplication brandApplication;

    /**
     * 관리자가 등록된 brand들의 승인 상태 '거부'로 변경.
     * @param brandId
     * @return String
     */
    @PatchMapping("/{brandId}/deny")
    public BaseResponse<String> denyBrandPost(@PathVariable Long brandId){
        brandApplication.denyBrandPost(brandId);
        return new BaseResponse<>("해당 브랜드 등록이 거부됐습니다.");
    }

    /**
     * 관리자가 등록된 brand들의 승인 상태 '승인'으로 변경.
     * @param brandId
     * @return String
     */
    @PatchMapping("/{brandId}/approve")
    public BaseResponse<String> approveBrandPost(@PathVariable Long brandId){
        brandApplication.approveBrandPost(brandId);
        return new BaseResponse<>("해당 브랜드 등록이 승인됐습니다.");
    }

    /**
     * 관리자가 등록된 brand들의 요청들을 상태별(AuthorityStatus) 페이징 조회할 수 있음.
     * @param authorityStatus
     * @param pageNo
     * @return List<ReadBrandRequestByStatusResponseDTO>
     */
    @GetMapping
    public BaseResponse<List<GetBrandRequestByStatusResponseDTO>> getBrandRequestsByStatus(
            @RequestParam(value = "authorityStatus", required = false) AuthorityStatus authorityStatus,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ){
        return new BaseResponse<>(brandApplication.getBrandRequestByStatus(authorityStatus, pageNo));
    }
}
