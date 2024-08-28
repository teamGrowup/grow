package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.product.persist.entity.MainCategory;
import org.boot.growup.product.persist.entity.SubCategory;
import org.boot.growup.product.persist.repository.MainCategoryRepository;
import org.boot.growup.product.persist.repository.SubCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") // 테스트 시 dev profile을 활성화시킴.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 기본 생성된 datasource 빈을 사용함.
class SubCategoryRepositoryTest {
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private MainCategoryRepository mainCategoryRepository;

    @Test
    public void saveSubCategory_success() {
        // given
        MainCategory mainCategory = MainCategory.builder()
                .name("상의")
                .build();
        mainCategoryRepository.save(mainCategory);

        SubCategory subCategory = SubCategory.builder()
                .name("티셔츠")
                .mainCategory(mainCategory) // 메인 카테고리 설정
                .build();

        // when
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);

        // then
        assertNotNull(savedSubCategory.getId());
        assertEquals("티셔츠", savedSubCategory.getName());
    }

    @Test
    public void findSubCategoryById_success() {
        // given
        MainCategory mainCategory = MainCategory.builder()
                .name("하의")
                .build();
        mainCategoryRepository.save(mainCategory);

        SubCategory subCategory = SubCategory.builder()
                .name("반바지")
                .mainCategory(mainCategory) // 메인 카테고리 설정
                .build();
        subCategoryRepository.save(subCategory);

        // when
        SubCategory foundSubCategory = subCategoryRepository.findById(subCategory.getId()).orElse(null);

        // then
        assertNotNull(foundSubCategory);
        assertEquals("반바지", foundSubCategory.getName());
    }
}
