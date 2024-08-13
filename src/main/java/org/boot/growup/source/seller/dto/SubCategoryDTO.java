package org.boot.growup.source.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDTO {

    private Long id;  // id 필드 추가

    @NotBlank(message = "서브 카테고리 이름은 필수입니다.")
    private String name;

    private MainCategoryDTO mainCategory; // 메인 카테고리 정보 추가
}

