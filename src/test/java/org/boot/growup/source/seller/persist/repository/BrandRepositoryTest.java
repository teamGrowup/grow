package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

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

    List<Brand> brandList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Brand brand1 = Brand.builder()
                .name("브랜드1")
                .description("브랜드1은 심플한 디자인과 고급스러운 소재를 활용한 제품을 선보입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(10)
                .build();

        Brand brand2 = Brand.builder()
                .name("브랜드2")
                .description("브랜드2는 혁신적인 기술과 전통적인 장인 정신을 결합하여 특별한 제품을 만듭니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(20)
                .build();

        Brand brand3 = Brand.builder()
                .name("브랜드3")
                .description("브랜드3은 자연에서 영감을 받은 컬렉션을 통해 지속 가능한 패션을 제안합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(15)
                .build();

        Brand brand4 = Brand.builder()
                .name("브랜드4")
                .description("브랜드4는 다양한 사람들이 함께 즐길 수 있는 편안하고 실용적인 제품을 전개합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(5)
                .build();

        Brand brand5 = Brand.builder()
                .name("브랜드5")
                .description("브랜드5는 세련된 디자인과 높은 품질로 일상에 특별함을 더합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(30)
                .build();

        Brand brand6 = Brand.builder()
                .name("브랜드6")
                .description("브랜드6은 정교한 디테일과 현대적인 감각을 반영한 패션 아이템을 제공합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(25)
                .build();

        Brand brand7 = Brand.builder()
                .name("브랜드7")
                .description("브랜드7은 글로벌 트렌드를 반영하여 다양한 스타일의 제품을 선보입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(12)
                .build();

        Brand brand8 = Brand.builder()
                .name("브랜드8")
                .description("브랜드8은 고유의 개성과 독창성을 바탕으로 개성 넘치는 제품을 제공합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(18)
                .build();

        Brand brand9 = Brand.builder()
                .name("브랜드9")
                .description("브랜드9은 실용성과 미적 감각을 동시에 만족시키는 제품을 제작합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(22)
                .build();

        Brand brand10 = Brand.builder()
                .name("브랜드10")
                .description("브랜드10은 기능성과 스타일을 모두 갖춘 현대적인 패션 아이템을 제공합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(8)
                .build();
        brandList.addAll(List.of(brand1, brand2
        , brand3, brand4
        , brand5, brand6
        , brand7, brand8
        , brand9, brand10
        ));
    }
    
    @Test
    public void save_success() {
        //given
        Brand brand = Brand.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(0)
                .build();

        //when
        Brand brandDB = brandRepository.save(brand);
        
        //then
        assertNotNull(brandDB);
        assertEquals(brand.getName(), brandDB.getName());
        assertEquals(brand.getDescription(), brandDB.getDescription());
        assertEquals(brand.getLikeCount(), brandDB.getLikeCount());
        assertEquals(brand.getId(), brandDB.getId());
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
                .likeCount(0)
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

    @Test
    public void findByAuthorityStatus_Default_Success(){
        //given
        Pageable pageable = PageRequest.of(0, 10);
        AuthorityStatus authorityStatus = AuthorityStatus.PENDING;
        brandRepository.saveAll(brandList);

        //when
        List<Brand> brandFoundList = brandRepository.findByAuthorityStatus(authorityStatus, pageable);

        //then
        assertEquals(brandFoundList.size(), 10);
        assertEquals(brandList.stream().map(Brand::getName).toList(),
                brandFoundList.stream().map(Brand::getName).toList());
        assertEquals(brandList.stream().map(Brand::getDescription).toList(),
                brandFoundList.stream().map(Brand::getDescription).toList());
    }

    @Test
    public void findByAuthorityStatus_NoStatus_Success(){
        //given
        Pageable pageable = PageRequest.of(0, 10);
        brandRepository.saveAll(brandList);

        //when
        List<Brand> brandFoundList = brandRepository.findAll(pageable).stream().toList();

        //then
        assertEquals(brandFoundList.size(), 10);
        assertEquals(brandList.stream().map(Brand::getName).toList(),
                brandFoundList.stream().map(Brand::getName).toList());
        assertEquals(brandList.stream().map(Brand::getDescription).toList(),
                brandFoundList.stream().map(Brand::getDescription).toList());
    }

}