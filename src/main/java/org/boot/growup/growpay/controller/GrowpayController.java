package org.boot.growup.growpay.controller;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.growpay.dto.request.GrowpayRequestDTO;
import org.boot.growup.growpay.dto.GrowpayHistoryDTO;
import org.boot.growup.growpay.persist.entity.Growpay;
import org.boot.growup.growpay.service.GrowpayService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers/growpay")
@RequiredArgsConstructor
public class GrowpayController {

    private final GrowpayService growpayService;

    @PostMapping
    public BaseResponse<Growpay> createGrowpay(@RequestBody GrowpayRequestDTO growpayRequestDTO) {
        Growpay newGrowpay = growpayService.createGrowpay(growpayRequestDTO);
        return new BaseResponse<>(newGrowpay);
    }

    // 결제 처리
    @PostMapping("/payments")
    public BaseResponse<String> processPayment(@RequestBody GrowpayRequestDTO growpayRequestDTO) {
        growpayService.processPayment(growpayRequestDTO);
        return new BaseResponse<>("결제가 완료되었습니다.");
    }

    // 환불 처리
    @PostMapping("/refunds/{historyId}")
    public BaseResponse<String> processRefund(@PathVariable Long historyId) {
        growpayService.processRefund(historyId);
        return new BaseResponse<>("환불이 완료되었습니다.");
    }

    // 거래 내역 조회
    @GetMapping("/history/{growpayId}")
    public BaseResponse<List<GrowpayHistoryDTO>> getTransactionHistory(@PathVariable Long growpayId) {
        List<GrowpayHistoryDTO> history = growpayService.getTransactionHistory(growpayId);
        return new BaseResponse<>(history);
    }

    // 잔액 조회
    @GetMapping("/balance/{growpayId}")
    public BaseResponse<Integer> getBalance(@PathVariable Long growpayId) {
        int balance = growpayService.getBalance(growpayId);
        return new BaseResponse<>(balance);
    }
}
