package org.boot.growup.source.seller.persist.repository;

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
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SellerRepositoryTest {

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    public void sellerInsert_success() {
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

        //when
        Seller sellerDB = sellerRepository.save(seller);

        //then
        assertNotNull(sellerDB);
        assertEquals(seller.getCpEmail(), sellerDB.getCpEmail());
        assertEquals(seller.getCpPassword(), sellerDB.getCpPassword());
        assertEquals(seller.getPhoneNumber(), sellerDB.getPhoneNumber());
        assertEquals(seller.getEpName(), sellerDB.getEpName());
        assertEquals(seller.getCpName(), sellerDB.getCpName());
        assertEquals(seller.getCpCode(), sellerDB.getCpCode());
        assertEquals(seller.getCpAddress(), sellerDB.getCpAddress());
        assertEquals(seller.getNetProceeds(), sellerDB.getNetProceeds());
        assertEquals(1L, sellerDB.getId());
    }


}