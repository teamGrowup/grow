package org.boot.growup.source.seller.service;

import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.RegisterBrandRequestDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BrandService {
    Brand registerBrand(RegisterBrandRequestDTO registerBrandRequestDTO, Seller seller);
    Brand readBrandBySellerId(Long sellerId);
    Brand updateBrand(RegisterBrandRequestDTO registerBrandRequestDTO, Seller seller);
    void changeBrandAuthority(Long brandId, AuthorityStatus status);
    List<Brand> readBrandRequestsByStatus(AuthorityStatus authorityStatus, int pageNo);
    Brand getBrandById(Long brandId);
}
