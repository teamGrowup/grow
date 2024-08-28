package org.boot.growup.growpay.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.growpay.dto.request.GrowpayRequestDTO;
import org.boot.growup.growpay.dto.response.GrowpayHistoryResponseDTO;
import org.boot.growup.growpay.persist.entity.Growpay;
import org.boot.growup.growpay.service.GrowpayService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/customers/growpay")
@RequiredArgsConstructor
public class GrowpayController {

    private final GrowpayService growpayService;

    /**
     * [POST]
     * Growpay 계좌 생성
     * @header Customer's AccessToken
     * @body GrowpayRequestDTO
     * @response Growpay
     */
    @PostMapping
    public BaseResponse<Growpay> createGrowpay(@RequestBody GrowpayRequestDTO growpayRequestDTO) {
        Growpay newGrowpay = growpayService.createGrowpay(growpayRequestDTO);
        return new BaseResponse<>(newGrowpay);
    }

    /**
     * [POST]
     * Growpay 계좌에 입금
     * @header Customer's AccessToken
     * @body GrowpayRequestDTO
     * @response String
     */
    @PostMapping("/deposit")
    public BaseResponse<String> processDeposit(@RequestBody GrowpayRequestDTO growpayRequestDTO) {
        growpayService.growpayDeposit(growpayRequestDTO);
        return new BaseResponse<>("입금이 완료되었습니다.");
    }

    /**
     * [POST]
     * Growpay 계좌에서 출금
     * @header Customer's AccessToken
     * @body GrowpayRequestDTO
     * @response String
     */
    @PostMapping("/withdraw")
    public BaseResponse<String> processWithdrawal(@RequestBody GrowpayRequestDTO growpayRequestDTO) {
        growpayService.growpayWithdraw(growpayRequestDTO);
        return new BaseResponse<>("출금이 완료되었습니다.");
    }

    /**
     * [GET]
     * 특정 Growpay 계좌의 입출금 내역 조회
     * @header Customer's AccessToken
     * @param growpayId 연결된 Growpay ID
     * @response List<GrowpayHistoryResponseDTO>
     */
    @GetMapping("/history/{growpayId}")
    public BaseResponse<List<GrowpayHistoryResponseDTO>> getTransactionHistory(@PathVariable Long growpayId) {
        List<GrowpayHistoryResponseDTO> history = growpayService.getTransactionHistory(growpayId);
        return new BaseResponse<>(history);
    }

    /**
     * [GET]
     * 특정 Growpay 계좌의 잔액 조회
     * @header Customer's AccessToken
     * @param growpayId 연결된 Growpay ID
     * @response Integer
     */
    @GetMapping("/balance/{growpayId}")
    public BaseResponse<Integer> getBalance(@PathVariable Long growpayId) {
        int balance = growpayService.getBalance(growpayId);
        return new BaseResponse<>(balance);
    }

    /**
     * [DELETE]
     * 특정 Growpay 계좌 삭제
     * @header Customer's AccessToken
     * @param growpayId 삭제할 Growpay ID
     * @response String
     */
    @DeleteMapping("/{growpayId}")
    public BaseResponse<String> deleteGrowpay(@PathVariable Long growpayId) {
        growpayService.deleteGrowpay(growpayId);
        return new BaseResponse<>("그로우페이가 삭제되었습니다.");
    }
}
