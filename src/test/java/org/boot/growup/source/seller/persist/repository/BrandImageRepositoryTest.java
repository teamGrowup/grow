package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.source.seller.constant.AuthorityStatus;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.BrandImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") // 테스트 시 dev profile을 활성화시킴.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 기본 생성된 datasource 빈을 사용함.
class BrandImageRepositoryTest {
    
    @Autowired
    private BrandImageRepository brandImageRepository;

    @Autowired
    private BrandRepository brandRepository;

    Brand brand;
    BrandImage brandImage1;
    BrandImage brandImage2;
    BrandImage brandImage3;

    @BeforeEach
    void setUp() {
        brandRepository.deleteAll();
        brandImageRepository.deleteAll();

        brand = Brand.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likes(0)
                .build();

        brandRepository.save(brand);

        brandImage1 = BrandImage.builder()
                .originalImageName("HelloWorld.jpg")
                .path("https://bucket.s3.region.amazonaws.com/HelloWorld.jpg")
                .brand(brand)
                .build();

        brandImage2 = BrandImage.builder()
                .originalImageName("HelloWorld2.jpg")
                .path("https://bucket.s3.region.amazonaws.com/HelloWorld2.jpg")
                .brand(brand)
                .build();

        brandImage3 = BrandImage.builder()
                .originalImageName("HelloWorld3.jpg")
                .path("https://bucket.s3.region.amazonaws.com/HelloWorld3.jpg")
                .brand(brand)
                .build();
    }

    @Test
    public void brandImageInsert_Default_Success() {
        //given
        BrandImage brandImage = BrandImage.builder()
                .originalImageName("HelloWorld.jpg")
                .path("https://bucket.s3.region.amazonaws.com/HelloWorld.jpg")
                .brand(brand)
                .build();

        //when
        BrandImage brandImageDB = brandImageRepository.save(brandImage);
        
        //then
        assertNotNull(brandImageDB);
        assertEquals("HelloWorld.jpg", brandImageDB.getOriginalImageName());
        assertEquals(brandImage.getPath(), brandImageDB.getPath());
        assertEquals(brand, brandImageDB.getBrand());
        assertEquals(1L, brandImageDB.getId());
    }

    @Test
    public void findBrandImageByBrand_Id_Default_Success(){
        //given
        brandImageRepository.save(brandImage1);
        brandImageRepository.save(brandImage2);
        brandImageRepository.save(brandImage3);

        //when
        List<BrandImage> brandImages = brandImageRepository.findBrandImageByBrand_Id(brand.getId());

        //then
        assertNotNull(brandImages);
        assertEquals(3, brandImages.size());
    }

    @Test
    public void deleteBrandImageByBrand_Id_Default_Success(){
        //given
        brandImageRepository.save(brandImage1);
        brandImageRepository.save(brandImage2);
        brandImageRepository.save(brandImage3);

        //when
        brandImageRepository.deleteBrandImageByBrand_Id(brand.getId());

        //then
        assertEquals(0, brandImageRepository.findBrandImageByBrand_Id(1L).size());
    }

}