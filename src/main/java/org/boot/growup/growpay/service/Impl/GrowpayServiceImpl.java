package org.boot.growup.growpay.service.Impl;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.growpay.dto.request.GrowpayRequestDTO;
import org.boot.growup.growpay.dto.response.GrowpayHistoryResponseDTO;
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
        Customer customer = customerRepository.findById(growpayRequestDTO.getCustomerId())
                .orElseThrow(() -> new BaseException(ErrorCode.CUSTOMER_NOT_FOUND));

        Growpay growpay = Growpay.builder()
                .accountName(growpayRequestDTO.getAccountName())
                .accountNumber(growpayRequestDTO.getAccountNumber())
                .customer(customer)
                .growpayBalance(growpayRequestDTO.getAmount())
                .build();

        return growpayRepository.save(growpay);
    }

    // 그로우페이 입금
    @Override
    public void growpayDeposit(GrowpayRequestDTO growpayRequestDTO) {
        Growpay growpay = growpayRepository.findById(growpayRequestDTO.getGrowpayId())
                .orElseThrow(() -> new BaseException(ErrorCode.GROWPAY_NOT_FOUND));

        if (growpay.getGrowpayBalance() < growpayRequestDTO.getAmount()) {
            throw new BaseException(ErrorCode.INSUFFICIENT_GROWPAY_BALANCE);
        }

        growpay.depositBalance(growpayRequestDTO.getAmount());

        growpayRepository.save(growpay);

        GrowpayHistory history = GrowpayHistory.builder()
                .amount(growpayRequestDTO.getAmount())
                .transactionStatus(TransactionStatus.DEPOSIT)
                .growpay(growpay)
                .build();

        growpayHistoryRepository.save(history);
    }

    // 그로우페이 출금
    @Override
    public void growpayWithdraw(GrowpayRequestDTO growpayRequestDTO) {
        Growpay growpay = growpayRepository.findById(growpayRequestDTO.getGrowpayId())
                .orElseThrow(() -> new BaseException(ErrorCode.GROWPAY_NOT_FOUND));

        if (growpay.getGrowpayBalance() < growpayRequestDTO.getAmount()) {
            throw new BaseException(ErrorCode.INSUFFICIENT_GROWPAY_BALANCE);
        }

        growpay.withdrawBalance(growpayRequestDTO.getAmount());

        growpayRepository.save(growpay);

        GrowpayHistory history = GrowpayHistory.builder()
                .amount(growpayRequestDTO.getAmount())
                .transactionStatus(TransactionStatus.WITHDRAW)
                .growpay(growpay)
                .build();

        growpayHistoryRepository.save(history);
    }

    @Override
    public List<GrowpayHistoryResponseDTO> getTransactionHistory(Long growpayId) {
        List<GrowpayHistory> histories = growpayHistoryRepository.findByGrowpayId(growpayId);
        return histories.stream()
                .map(history -> GrowpayHistoryResponseDTO.builder()
                        .id(history.getId())
                        .amount(history.getAmount())
                        .createdAt(history.getCreatedAt())
                        .transactionStatus(history.getTransactionStatus())
                        .growpayId(history.getGrowpay().getId())
                        .build())
                .toList();
    }

    public int getBalance(Long growpayId) {
        Growpay growpay = growpayRepository.findById(growpayId)
                .orElseThrow(() -> new BaseException(ErrorCode.GROWPAY_NOT_FOUND));
        return growpay.getGrowpayBalance();
    }
}