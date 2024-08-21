package org.boot.growup.product.dto;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
public class SubCategoryDTO {
    private Long id;  // id 필드 추가

    @NotBlank(message = "서브 카테고리 이름은 필수입니다.")
    private String name;

    private MainCategoryDTO mainCategory; // 메인 카테고리 정보 추가

    // public 생성자 추가
    public SubCategoryDTO(Long id, String name, MainCategoryDTO mainCategory) {
        this.id = id;
        this.name = name;
        this.mainCategory = mainCategory;
    }
}

