package org.boot.growup.source.seller.service;

import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.RegisterBrandRequestDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.boot.growup.source.seller.persist.repository.BrandRepository;
import org.boot.growup.source.seller.persist.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BrandServiceImplTest {

    @Autowired
    private BrandServiceImpl brandServiceImpl;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SellerRepository sellerRepository;

    RegisterBrandRequestDTO registerBrandRequestDTO1;
    RegisterBrandRequestDTO registerBrandRequestDTO2;
    RegisterBrandRequestDTO registerBrandRequestDTO3;
    Seller seller;
    Seller seller2;

    @BeforeEach
    void setUp() {
        sellerRepository.deleteAll();  // 기존 데이터 삭제
        brandRepository.deleteAll();

        registerBrandRequestDTO1 = RegisterBrandRequestDTO.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .build();
        registerBrandRequestDTO2 = RegisterBrandRequestDTO.builder()
                .name("드로우핏")
                .description("드로우핏(DRAW FIT)은 핏과 착용감을 중요하게 여기며 컨템포러리&모던 룩을 지향하는 브랜드입니다. 뛰어난 퀄리티의 좋은 소재를 다양하게 구성해 바느질까지 세심하게 신경 써서 완성하고 고유의 핏과 무드를 머금은 웨어러블한 디자인의 아이템을 합리적인 가격으로 소개합니다.")
                .build();
        registerBrandRequestDTO3 = RegisterBrandRequestDTO.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .build();
        seller = Seller.builder()
                .cpEmail("lafudgestore@naver.com")
                .cpPassword("password1234")
                .phoneNumber("010-7797-8841") // 대표 전화번호
                .epName("손준호") // 대표자명
                .cpName("(주)슬로우스탠다드") // 상호명
                .cpCode("178-86-01188") // 10자리의 사업자 등록번호
                .cpAddress("경기도 의정부시 오목로225번길 94, 씨와이파크 (민락동)") // 사업장 소재지(회사주소)
                .netProceeds(1000)
                .build();
        seller2 = Seller.builder()
                .cpEmail("drawfit@naver.com")
                .cpPassword("password1234")
                .phoneNumber("02-3394-8271") // 대표 전화번호
                .epName("조현민") // 대표자명
                .cpName("디알에프티 주식회사") // 상호명
                .cpCode("722-87-00697") // 10자리의 사업자 등록번호
                .cpAddress("서울특별시 성동구 자동차시장1길 81, FCN빌딩 5층 (용답동)") // 사업장 소재지(회사주소)
                .netProceeds(1000)
                .build();
    }

    @Test
    void registerBrand_Default_Success() {
        //given
        sellerRepository.save(seller);

        //when
        brandServiceImpl.registerBrand(registerBrandRequestDTO1, seller);
        Brand brand = brandRepository.findById(seller.getId()).get();

        //then
        assertNotNull(brand);
        assertEquals(registerBrandRequestDTO1.getName(), brand.getName());
        assertEquals(registerBrandRequestDTO1.getDescription(), brand.getDescription());
    }

    @Test
    void registerBrand_DuplicateName_ThrowsIllegalStateException() {
        //given
        sellerRepository.save(seller);
        sellerRepository.save(seller2);
        brandServiceImpl.registerBrand(registerBrandRequestDTO1, seller);

        //when
        BaseException e = assertThrows(BaseException.class, () -> brandServiceImpl.registerBrand(registerBrandRequestDTO3, seller2));

        //then
        assertEquals(e.getErrorCode(), ErrorCode.BRAND_NAME_ALREADY_EXISTS);
    }

    @Test
    public void readBrandBySellerId_Default_Success() {
        //given
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
        Brand brandFound = brandServiceImpl.readBrandBySellerId(seller.getId());

        //then
        assertNotNull(brandFound);
        assertEquals(brand.getName(), brandFound.getName());

    }

    @Test
    public void readBrandBySellerId_SellerHasNotBrand_ThrowBaseException() {
        //given
        //when
        BaseException e = assertThrows(BaseException.class, () -> brandServiceImpl.readBrandBySellerId(2L));

        //then
        assertEquals(e.getErrorCode(), ErrorCode.BRAND_BY_SELLER_NOT_FOUND);
    }

    @Test
    public void updateBrand_Default_Success() {
        //given
        sellerRepository.save(seller);
        brandServiceImpl.registerBrand(registerBrandRequestDTO1, seller);

        //when
        brandServiceImpl.updateBrand(registerBrandRequestDTO3, seller);
        Brand brand = brandRepository.findBySeller_Id(1L).get();

        //then
        assertNotNull(brand);
        assertEquals(registerBrandRequestDTO3.getName(), brand.getName());
        assertEquals(registerBrandRequestDTO3.getDescription(), brand.getDescription());

    }
}