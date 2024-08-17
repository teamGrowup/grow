package org.boot.growup.source.seller.dto;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
public class MainCategoryDTO {
    private Long id;  // 메인 카테고리 ID

    @NotBlank(message = "메인 카테고리 이름은 필수입니다.")
    private String name;  // 메인 카테고리 이름

    // public 생성자 추가
    public MainCategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
