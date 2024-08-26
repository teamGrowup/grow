package org.boot.growup.growpay.service;

import org.boot.growup.growpay.dto.request.GrowpayRequestDTO;
import org.boot.growup.growpay.dto.response.GrowpayHistoryResponseDTO;
import org.boot.growup.growpay.persist.entity.Growpay;

import java.util.List;

public interface GrowpayService {
    /*
    그로우페이 생성
     */
    Growpay createGrowpay(GrowpayRequestDTO growpayRequestDTO);

    /*
    그로우페이에 입금
     */
    void growpayDeposit(GrowpayRequestDTO growpayRequestDTO);

    /*
    그로우페이에서 출금
     */
    void growpayWithdraw(GrowpayRequestDTO growpayRequestDTO);

    /*
    특정 Growpay ID로 거래 내역 조회
     */
    List<GrowpayHistoryResponseDTO> getTransactionHistory(Long growpayId);

    /*
    Growpay ID로 잔액 조회
     */
    int getBalance(Long growpayId);
}
