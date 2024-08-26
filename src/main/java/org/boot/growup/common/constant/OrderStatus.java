package org.boot.growup.common.constant;

public enum OrderStatus {
    /* 결제 관련 */
    PRE_PAYED, // 주문 번호 생성된 상태
    PAYED, // Order 객체 생성 및 결제 완료된 상태
    REJECTED, // PG사 결제 거부 상태 (결제 실패)
    CANCELED, // 결제 도중 구매자가 결제 중단 혹은 해당 주문을 취소한 상태

    /* 상품 출고 관련 */
    PRE_SHIPPED, // 출고 대기중
    SHIPPED, // 출고 완료

    /* 배송 관련 */
    PENDING_SHIPMENT, // 운송장 번호 등록 및 택배 접수 완료
    IN_TRANSIT, // 배송 중
    ARRIVED, // 배송 완료
    HOLDING, // 배송 보류 중

    /* 구매 완료 관련 */
    PURCHASE_CONFIRM, // 구매 확정
    PRE_EXCHANGE, // 교환 접수
    PRE_REFUND // 환불 접수

}
