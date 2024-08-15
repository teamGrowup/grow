package org.boot.growup.source.seller.service;

import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.PostBrandRequestDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BrandService {
    /*
    Seller 정보로 Brand 등록하기
     */
    Brand postBrand(PostBrandRequestDTO postBrandRequestDTO, Seller seller);

    /*
    SellerId로 Brand 정보 가져오기
     */
    Brand getBrandBySellerId(Long sellerId);

    /*
    Seller 정보로 등록된 Brand 수정하기
     */
    Brand patchBrand(PostBrandRequestDTO postBrandRequestDTO, Seller seller);

    /*
    Brand 허가 상태 변경하기
     */
    void changeBrandAuthority(Long brandId, AuthorityStatus status);

    /*
    Brand 등록 상태에 따른 Brand 등록 현황 조회
     */
    List<Brand> getBrandRequestsByStatus(AuthorityStatus authorityStatus, int pageNo);

    /*
    BrnadId로 Brand 가져오기
     */
    Brand getBrandById(Long brandId);
}
