package org.boot.growup.source.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainCategoryDTO {

    private Long id;  // 메인 카테고리 ID

    @NotBlank(message = "메인 카테고리 이름은 필수입니다.")
    private String name;  // 메인 카테고리 이름
}
