package org.boot.growup.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.order.application.OrderApplication;
import org.boot.growup.order.dto.request.ProcessNormalOrderRequestDTO;
import org.boot.growup.order.dto.response.GetOrderHistoryResponseDTO;
import org.boot.growup.order.dto.response.GetOrderResponseDTO;
import org.boot.growup.order.dto.response.ProcessNormalOrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customers/orders")
@RequiredArgsConstructor
public class CustomerOrderController {
    private final OrderApplication orderApplication;

    /**
     * [POST]
     * PG사 결제 이전에, 주문 번호를 요청함.
     * @header CustomerAccesstoken
     * @body ProcessNormalOrderRequestDTO 주문 정보
     * @response merchantUid
     */
    @PostMapping("/payments/process")
    public BaseResponse<ProcessNormalOrderResponseDTO> processNormalOrder(@Valid @RequestBody ProcessNormalOrderRequestDTO form) {
        form.getOrderItemDTOs().forEach(m -> log.info(String.valueOf(m.getProductOptionId())));
        return new BaseResponse<>(orderApplication.processNormalOrder(form));
    }

    /**
     * [PATCH]
     * PG사에 결제 성공 사실을 알림.
     * @header CustomerAccesstoken
     * @param merchantUid 주문 번호
     */
    @PatchMapping("/payments/complete/{merchantUid}")
    public void completeNormalOrder(@PathVariable String merchantUid){
        orderApplication.completeNormalOrder(merchantUid);
    }

    /**
     * [PATCH]
     * PG사에 결제 실패 사실을 알림.
     * @header CustomerAccesstoken
     * @param merchantUid 주문 번호
     */
    @PatchMapping("/payments/rejected/{merchantUid}")
    public void rejectNormalOrder(@PathVariable String merchantUid){
        orderApplication.rejectNormalOrder(merchantUid);
    }

    /**
     * [PATCH]
     * 구매자가 해당 orderItem을 구매 확정 상태로 변경함.
     * @header CustomerAccesstoken
     * @param merchantUid 주문 번호
     * @param orderItemId 주문 항목ID
     */
    @PatchMapping("/purchase-confirmed/{merchantUid}/{orderItemId}")
    public void patchPurchaseConfirmed(
            @PathVariable String merchantUid,
            @PathVariable Long orderItemId
    ){
        orderApplication.patchPurchaseConfirmed(merchantUid, orderItemId);
    }

    /**
     * [GET]
     * 구매자의 주문 내역 단일 조회
     * @header CustomerAccesstoken
     * @param merchantUid 주문 번호
     */
    @GetMapping("/{merchantUid}")
    public BaseResponse<GetOrderResponseDTO> getOrder(@PathVariable String merchantUid){
        return new BaseResponse<>(orderApplication.getOrder(merchantUid));
    }

    /**
     * [GET]
     * 구매자의 주문 내역 목록 조회
     * @header CustomerAccesstoken
     * @param pageNo 페이지번호
     * @return Page<GetOrderHistoryResponseDTO>
     */
    @GetMapping("/history")
    public BaseResponse<Page<GetOrderHistoryResponseDTO>> getOrderHistory(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ){
        return new BaseResponse<>(orderApplication.getOrderHistory(pageNo));
    }
    // 환불 요청
    // 그로우페이 결제
}
