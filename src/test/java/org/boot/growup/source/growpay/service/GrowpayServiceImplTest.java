package org.boot.growup.source.growpay.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.boot.growup.common.constant.*;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.growpay.dto.request.GrowpayRequestDTO;
import org.boot.growup.growpay.dto.response.GrowpayHistoryResponseDTO;
import org.boot.growup.growpay.persist.entity.Growpay;
import org.boot.growup.growpay.persist.entity.GrowpayHistory;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.growpay.persist.repository.GrowpayRepository;
import org.boot.growup.growpay.persist.repository.GrowpayHistoryRepository;
import org.boot.growup.auth.persist.repository.CustomerRepository;
import org.boot.growup.growpay.service.Impl.GrowpayServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GrowpayServiceImplTest {

    @Mock
    private GrowpayRepository growpayRepository;

    @Mock
    private GrowpayHistoryRepository growpayHistoryRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private GrowpayServiceImpl growpayService;

    private Customer customer;
    private Growpay growpay;
    private GrowpayRequestDTO growpayRequestDTO;

    @BeforeEach
    public void setUp() {
        customer = Customer.builder()
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

        growpay = Growpay.builder()
                .id(1L)
                .accountName("Test Account")
                .accountNumber("123-456-789")
                .customer(customer)
                .growpayBalance(1000)
                .build();

        growpayRequestDTO = GrowpayRequestDTO.builder()
                .growpayId(1L)
                .amount(500)
                .accountName("Test Account")
                .accountNumber("123-456-789")
                .customerId(1L)
                .build();
    }

    @Test
    public void testCreateGrowpay() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(growpayRepository.save(any(Growpay.class))).thenReturn(growpay);

        Growpay createdGrowpay = growpayService.createGrowpay(growpayRequestDTO);

        assertNotNull(createdGrowpay);
        assertEquals("Test Account", createdGrowpay.getAccountName());
        verify(growpayRepository, times(1)).save(any(Growpay.class));
    }

    @Test
    public void testGrowpayDeposit() {
        when(growpayRepository.findById(1L)).thenReturn(Optional.of(growpay));

        growpayService.growpayDeposit(growpayRequestDTO);

        assertEquals(1500, growpay.getGrowpayBalance());
        verify(growpayRepository, times(1)).save(growpay);
        verify(growpayHistoryRepository, times(1)).save(any(GrowpayHistory.class));
    }

    @Test
    public void testGrowpayWithdraw() {
        when(growpayRepository.findById(1L)).thenReturn(Optional.of(growpay));

        growpayService.growpayWithdraw(growpayRequestDTO);

        assertEquals(500, growpay.getGrowpayBalance());
        verify(growpayRepository, times(1)).save(growpay);
        verify(growpayHistoryRepository, times(1)).save(any(GrowpayHistory.class));
    }

    @Test
    public void testGetTransactionHistory() {
        GrowpayHistory history = GrowpayHistory.builder()
                .id(1L)
                .amount(500)
                .transactionStatus(TransactionStatus.DEPOSIT)
                .growpay(growpay)
                .build();

        when(growpayHistoryRepository.findByGrowpayId(1L)).thenReturn(Collections.singletonList(history));

        List<GrowpayHistoryResponseDTO> historyResponse = growpayService.getTransactionHistory(1L);

        assertEquals(1, historyResponse.size());
        assertEquals(500, historyResponse.get(0).getAmount());
    }

    @Test
    public void testGetBalance() {
        when(growpayRepository.findById(1L)).thenReturn(Optional.of(growpay));

        int balance = growpayService.getBalance(1L);

        assertEquals(1000, balance);
    }

    @Test
    public void testCreateGrowpay_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        BaseException exception = assertThrows(BaseException.class, () -> {
            growpayService.createGrowpay(growpayRequestDTO);
        });

        assertEquals(ErrorCode.CUSTOMER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void testGrowpayWithdraw_InsufficientBalance() {
        // 출금 요청 금액을 잔액보다 많게 설정
        growpayRequestDTO.setAmount(1500);

        when(growpayRepository.findById(1L)).thenReturn(Optional.of(growpay));

        BaseException exception = assertThrows(BaseException.class, () -> {
            growpayService.growpayWithdraw(growpayRequestDTO);
        });

        assertEquals(ErrorCode.INSUFFICIENT_GROWPAY_BALANCE, exception.getErrorCode());
    }
}
