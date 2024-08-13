package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.source.seller.persist.entity.MainCategory;
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
class MainCategoryRepositoryTest {

    @Autowired
    private MainCategoryRepository mainCategoryRepository;

    @Test
    public void saveMainCategory_success() {
        // given
        MainCategory mainCategory = MainCategory.builder()
                .name("상의")
                .build();

        // when
        MainCategory savedCategory = mainCategoryRepository.save(mainCategory);

        // then
        assertNotNull(savedCategory.getId());
        assertEquals("상의", savedCategory.getName());
    }

    @Test
    public void findMainCategoryById_success() {
        // given
        MainCategory mainCategory = MainCategory.builder()
                .name("하의")
                .build();
        mainCategoryRepository.save(mainCategory);

        // when
        MainCategory foundCategory = mainCategoryRepository.findById(mainCategory.getId()).orElse(null);

        // then
        assertNotNull(foundCategory);
        assertEquals("하의", foundCategory.getName());
    }
}
