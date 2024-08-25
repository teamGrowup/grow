package org.boot.growup.growpay.service;

import org.boot.growup.growpay.dto.request.GrowpayRequestDTO;
import org.boot.growup.growpay.dto.GrowpayHistoryDTO;
import org.boot.growup.growpay.persist.entity.Growpay;

import java.util.List;

public interface GrowpayService {

    Growpay createGrowpay(GrowpayRequestDTO growpayRequestDTO);

    /*
    결제 처리 메서드
     */
    void processPayment(GrowpayRequestDTO growpayRequestDTO);

    /*
    환불 처리 메서드
     */
    void processRefund(Long historyId);

    /*
    특정 Growpay ID로 거래 내역 조회
     */
    List<GrowpayHistoryDTO> getTransactionHistory(Long growpayId);

    /*
    특정 Growpay ID로 잔액 조회
     */
    int getBalance(Long growpayId);
}
