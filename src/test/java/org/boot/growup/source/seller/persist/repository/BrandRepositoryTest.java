package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") // 테스트 시 dev profile을 활성화시킴.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 기본 생성된 datasource 빈을 사용함.
class BrandRepositoryTest {
    
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SellerRepository sellerRepository;
    
    @Test
    public void save_success() {
        //given
        Brand brand = Brand.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likes(0)
                .build();

        //when
        Brand brandDB = brandRepository.save(brand);
        
        //then
        assertNotNull(brandDB);
        assertEquals(brand.getName(), brandDB.getName());
        assertEquals(brand.getDescription(), brandDB.getDescription());
        assertEquals(brand.getLikes(), brandDB.getLikes());
        assertEquals(1L, brandDB.getId());
    }

    @Test
    public void findBySeller_Id_Default_Success() {
        //given
        Seller seller = Seller.builder()
                .cpEmail("lafudgestore@naver.com")
                .cpPassword("password1234")
                .phoneNumber("010-7797-8841") // 대표 전화번호
                .epName("손준호") // 대표자명
                .cpName("(주)슬로우스탠다드") // 상호명
                .cpCode("178-86-01188") // 10자리의 사업자 등록번호
                .cpAddress("경기도 의정부시 오목로225번길 94, 씨와이파크 (민락동)") // 사업장 소재지(회사주소)
                .netProceeds(1000)
                .build();

        Brand brand = Brand.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likes(0)
                .seller(seller)
                .build();

        sellerRepository.save(seller);
        brandRepository.save(brand);

        //when
        Brand brandFound = brandRepository.findBySeller_Id(1L).get();

        //then
        assertNotNull(brandFound);
        assertEquals(brand.getName(), brandFound.getName());

    }

}