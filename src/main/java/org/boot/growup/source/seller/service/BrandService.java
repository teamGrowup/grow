package org.boot.growup.source.seller.service;

import org.boot.growup.source.seller.dto.request.RegisterBrandRequestDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.springframework.stereotype.Service;

@Service
public interface BrandService {
    Brand registerBrand(RegisterBrandRequestDTO registerBrandRequestDTO);
}
