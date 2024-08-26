package org.boot.growup.source.growpay.persist.repository;

import org.boot.growup.auth.persist.repository.CustomerRepository;
import org.boot.growup.common.constant.Gender;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.common.constant.Role;
import org.boot.growup.growpay.persist.entity.Growpay;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.growpay.persist.repository.GrowpayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") // 테스트 시 test profile을 활성화
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional // 각 테스트 후 롤백
public class GrowpayRepositoryTest {
    @Autowired
    private GrowpayRepository growpayRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Growpay growpay;

    @BeforeEach
    public void setUp() {
        Customer customer = Customer.builder() // Customer 객체 초기화
                .email("customer123@naver.com")
                .password("!a123456789")
                .phoneNumber("010-1234-5678")
                .birthday("20001212")
                .gender(Gender.MALE)
                .address("용인시 기흥구 신정로 19")
                .postCode("12345")
                .nickname("오리")
                .name("홍길동")
                .provider(Provider.EMAIL)
                .role(Role.CUSTOMER)
                .profileUrl("awss3/이미지url")
                .isValidPhoneNumber(true)
                .isValidEmail(false)
                .isAgreeSendEmail(false)
                .isAgreeSendSms(false)
                .build();

        customerRepository.save(customer); // Customer 저장

        growpay = Growpay.builder()
                .accountName("Test Account")
                .accountNumber("123-456-789")
                .customer(customer) // 저장된 Customer 사용
                .growpayBalance(1000)
                .build();

        growpayRepository.save(growpay); // 테스트 데이터 저장
    }

    @Test
    public void testFindById() {
        Optional<Growpay> foundGrowpay = growpayRepository.findById(growpay.getId());
        assertTrue(foundGrowpay.isPresent());
        assertEquals(growpay.getAccountName(), foundGrowpay.get().getAccountName());
    }

    @Test
    public void testSaveGrowpay() {
        Customer newCustomer = Customer.builder() // Customer 객체 초기화
                .email("customer123@naver.com")
                .password("!a123456789")
                .phoneNumber("010-1234-5678")
                .birthday("20001212")
                .gender(Gender.MALE)
                .address("용인시 기흥구 신정로 19")
                .postCode("12345")
                .nickname("오리")
                .name("홍길동")
                .provider(Provider.EMAIL)
                .role(Role.CUSTOMER)
                .profileUrl("awss3/이미지url")
                .isValidPhoneNumber(true)
                .isValidEmail(false)
                .isAgreeSendEmail(false)
                .isAgreeSendSms(false)
                .build();

        customerRepository.save(newCustomer); // Customer 저장

        Growpay newGrowpay = Growpay.builder()
                .accountName("New Account")
                .accountNumber("987-654-321")
                .customer(newCustomer) // 기존 Customer 사용
                .growpayBalance(2000)
                .build();

        Growpay savedGrowpay = growpayRepository.save(newGrowpay);
        assertNotNull(savedGrowpay.getId());
        assertEquals("New Account", savedGrowpay.getAccountName());
    }

    @Test
    public void testDeleteGrowpay() {
        growpayRepository.delete(growpay);
        Optional<Growpay> deletedGrowpay = growpayRepository.findById(growpay.getId());
        assertFalse(deletedGrowpay.isPresent());
    }

    @Test
    public void testSaveGrowpay_WithNewCustomer() {
        Customer newCustomer = Customer.builder() // 새로운 Customer 객체 초기화
                .email("newcustomer@naver.com")
                .password("!a123456789")
                .phoneNumber("010-9876-5432")
                .birthday("20001212")
                .gender(Gender.FEMALE)
                .address("용인시 기흥구 신정로 20")
                .postCode("12346")
                .nickname("여우")
                .name("김영희")
                .provider(Provider.EMAIL)
                .role(Role.CUSTOMER)
                .profileUrl("awss3/새이미지url")
                .isValidPhoneNumber(true)
                .isValidEmail(true)
                .isAgreeSendEmail(true)
                .isAgreeSendSms(true)
                .build();

        customerRepository.save(newCustomer); // 새로운 Customer 저장

        Growpay newGrowpay = Growpay.builder()
                .accountName("Another Account")
                .accountNumber("111-222-333")
                .customer(newCustomer) // 새로운 Customer 사용
                .growpayBalance(1500)
                .build();

        Growpay savedGrowpay = growpayRepository.save(newGrowpay);
        assertNotNull(savedGrowpay.getId());
        assertEquals("Another Account", savedGrowpay.getAccountName());
    }
}
