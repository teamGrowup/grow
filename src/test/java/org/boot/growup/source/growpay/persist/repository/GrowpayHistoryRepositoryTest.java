package org.boot.growup.source.growpay.persist.repository;

import org.boot.growup.auth.persist.repository.CustomerRepository;
import org.boot.growup.common.constant.Gender;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.common.constant.Role;
import org.boot.growup.common.constant.TransactionStatus;
import org.boot.growup.growpay.persist.entity.Growpay;
import org.boot.growup.growpay.persist.entity.GrowpayHistory;
import org.boot.growup.growpay.persist.repository.GrowpayHistoryRepository;
import org.boot.growup.growpay.persist.repository.GrowpayRepository;
import org.boot.growup.auth.persist.entity.Customer;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GrowpayHistoryRepositoryTest {
    @Autowired
    private GrowpayHistoryRepository growpayHistoryRepository;

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
                .customer(customer)
                .growpayBalance(1000)
                .build();

        growpayRepository.save(growpay); // 테스트 데이터 저장

        // GrowpayHistory 데이터 저장
        GrowpayHistory history = GrowpayHistory.builder()
                .amount(500)
                .transactionStatus(TransactionStatus.DEPOSIT)
                .growpay(growpay)
                .build();

        growpayHistoryRepository.save(history);
    }

    @Test
    public void testFindByGrowpayId() {
        List<GrowpayHistory> histories = growpayHistoryRepository.findByGrowpayId(growpay.getId());
        assertEquals(1, histories.size());
        assertEquals(500, histories.get(0).getAmount());
    }

    @Test
    public void testFindByGrowpayId_NoHistory() {
        List<GrowpayHistory> histories = growpayHistoryRepository.findByGrowpayId(999L); // 존재하지 않는 ID
        assertTrue(histories.isEmpty());
    }
}
