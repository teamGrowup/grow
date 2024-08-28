package org.boot.growup.source.growpay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.boot.growup.common.constant.TransactionStatus;
import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.common.utils.RequestMatcherHolder;
import org.boot.growup.growpay.controller.GrowpayController;
import org.boot.growup.growpay.dto.request.GrowpayRequestDTO;
import org.boot.growup.growpay.dto.response.GrowpayHistoryResponseDTO;
import org.boot.growup.growpay.persist.entity.Growpay;
import org.boot.growup.growpay.service.GrowpayService;
import org.boot.growup.auth.utils.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(GrowpayController.class)
@ActiveProfiles("test") // test profile 활성화
@WithMockUser(roles = "CUSTOMER") // Mock 사용자로 테스트
@ContextConfiguration(classes = {GrowpayController.class, RequestMatcherHolder.class})
public class GrowpayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GrowpayService growpayService;

    @MockBean
    private JwtTokenProvider tokenProvider; // 필요에 따라 Mock 설정
    @MockBean
    private RequestMatcherHolder requestMatcherHolder;

    private String token;

    @BeforeEach
    void setUp() {
        // TokenProvider의 동작을 모킹
        when(tokenProvider.generateToken(any(String.class), any())).thenReturn(TokenDTO.of("mocked-access-token", "mocked-refresh-token"));

        // Token 생성
        token = "Bearer " + "mocked-access-token"; // Mock된 액세스 토큰 사용
        System.out.println("Generated token: " + token);
    }

    @Test
    public void testCreateGrowpay() throws Exception {
        GrowpayRequestDTO requestDTO = GrowpayRequestDTO.builder()
                .customerId(1L)
                .accountName("Test Account")
                .accountNumber("123-456-789")
                .amount(1000)
                .build();

        Growpay createdGrowpay = Growpay.builder()
                .id(1L)
                .accountName("Test Account")
                .accountNumber("123-456-789")
                .growpayBalance(1000)
                .build();

        when(growpayService.createGrowpay(any(GrowpayRequestDTO.class))).thenReturn(createdGrowpay);

        mockMvc.perform(post("/customers/growpay")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountName").value("Test Account"));
    }

    @Test
    public void testProcessDeposit() throws Exception {
        GrowpayRequestDTO requestDTO = GrowpayRequestDTO.builder()
                .growpayId(1L)
                .amount(500)
                .build();

        // void 메서드의 경우 doNothing() 사용
        doNothing().when(growpayService).growpayDeposit(any(GrowpayRequestDTO.class));

        mockMvc.perform(post("/customers/growpay/deposit")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("입금이 완료되었습니다."));

        verify(growpayService).growpayDeposit(any(GrowpayRequestDTO.class));
    }

    @Test
    public void testProcessWithdraw() throws Exception {
        GrowpayRequestDTO requestDTO = GrowpayRequestDTO.builder()
                .growpayId(1L)
                .amount(500)
                .build();

        // 서비스 메서드의 반환값을 설정
        doNothing().when(growpayService).growpayWithdraw(any(GrowpayRequestDTO.class));

        mockMvc.perform(post("/customers/growpay/withdraw")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("출금이 완료되었습니다."));

        verify(growpayService).growpayWithdraw(any(GrowpayRequestDTO.class));
    }

    @Test
    public void testGetTransactionHistory() throws Exception {
        List<GrowpayHistoryResponseDTO> history = Collections.singletonList(
                GrowpayHistoryResponseDTO.builder()
                        .id(1L)
                        .amount(500)
                        .createdAt(LocalDateTime.now())
                        .transactionStatus(TransactionStatus.DEPOSIT)
                        .growpayId(1L)
                        .build()
        );

        when(growpayService.getTransactionHistory(1L)).thenReturn(history);

        mockMvc.perform(get("/customers/growpay/history/1")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].amount").value(500))
                .andExpect(jsonPath("$.data[0].transactionStatus").value("DEPOSIT"));

        verify(growpayService).getTransactionHistory(1L);
    }

    @Test
    public void testGetBalance() throws Exception {
        when(growpayService.getBalance(1L)).thenReturn(1000);

        mockMvc.perform(get("/customers/growpay/balance/1")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1000));

        verify(growpayService).getBalance(1L);
    }
}
