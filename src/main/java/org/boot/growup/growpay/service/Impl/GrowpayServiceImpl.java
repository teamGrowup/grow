package org.boot.growup.growpay.service.Impl;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.growpay.dto.request.GrowpayRequestDTO;
import org.boot.growup.growpay.dto.GrowpayHistoryDTO;
import org.boot.growup.growpay.persist.entity.Growpay;
import org.boot.growup.growpay.persist.entity.GrowpayHistory;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.common.constant.TransactionStatus;
import org.boot.growup.growpay.persist.repository.GrowpayRepository;
import org.boot.growup.growpay.persist.repository.GrowpayHistoryRepository;
import org.boot.growup.auth.persist.repository.CustomerRepository;
import org.boot.growup.growpay.service.GrowpayService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrowpayServiceImpl implements GrowpayService {

    private final GrowpayRepository growpayRepository;
    private final GrowpayHistoryRepository growpayHistoryRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Growpay createGrowpay(GrowpayRequestDTO growpayRequestDTO) {
        // 고객 ID로 고객 조회
        Customer customer = customerRepository.findById(growpayRequestDTO.getCustomerId())
                .orElseThrow(() -> new BaseException(ErrorCode.CUSTOMER_NOT_FOUND));

        // Growpay 객체 생성
        Growpay growpay = Growpay.builder()
                .accountName(growpayRequestDTO.getAccountName())
                .accountNumber(growpayRequestDTO.getAccountNumber())
                .customer(customer)
                .growpayBalance(growpayRequestDTO.getAmount())
                .build();

        // 저장
        return growpayRepository.save(growpay);
    }

    @Override
    public void processPayment(GrowpayRequestDTO growpayRequestDTO) {
        Growpay growpay = growpayRepository.findById(growpayRequestDTO.getGrowpayId())
                .orElseThrow(() -> new BaseException(ErrorCode.GROWPAY_NOT_FOUND));

        if (growpay.getGrowpayBalance() < growpayRequestDTO.getAmount()) {
            throw new BaseException(ErrorCode.INSUFFICIENT_GROWPAY_BALANCE);
        }

        // 잔액 차감
        growpay.paymentBalance(growpayRequestDTO.getAmount());

        // Growpay 엔티티 저장
        growpayRepository.save(growpay);

        // 거래 기록 저장
        GrowpayHistory history = GrowpayHistory.builder()
                .amount(growpayRequestDTO.getAmount())
                .transactionStatus(TransactionStatus.PAYMENT)
                .growpayId(growpay.getId())
                .build();

        growpayHistoryRepository.save(history);
    }

    @Override
    public void processRefund(Long historyId) {
        GrowpayHistory history = growpayHistoryRepository.findById(historyId)
                .orElseThrow(() -> new BaseException(ErrorCode.GROWPAY_HISTORY_NOT_FOUND));

        Growpay growpay = growpayRepository.findById(history.getGrowpayId())
                .orElseThrow(() -> new BaseException(ErrorCode.GROWPAY_NOT_FOUND));

        // 잔액에 환불 금액 추가
        growpay.refundBalance(history.getAmount());

        // Growpay 엔티티 저장
        growpayRepository.save(growpay);

        // 거래 기록 업데이트
        history.refund();
        growpayHistoryRepository.save(history);
    }

    @Override
    public List<GrowpayHistoryDTO> getTransactionHistory(Long growpayId) {
        List<GrowpayHistory> histories = growpayHistoryRepository.findByGrowpayId(growpayId);
        return histories.stream()
                .map(history -> GrowpayHistoryDTO.builder()
                        .id(history.getId())
                        .amount(history.getAmount())
                        .createdAt(history.getCreatedAt())
                        .transactionStatus(history.getTransactionStatus())
                        .growpayId(history.getGrowpayId())
                        .build())
                .toList();
    }

    public int getBalance(Long growpayId) {
        Growpay growpay = growpayRepository.findById(growpayId)
                .orElseThrow(() -> new BaseException(ErrorCode.GROWPAY_NOT_FOUND));
        return growpay.getGrowpayBalance();
    }
}