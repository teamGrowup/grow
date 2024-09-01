package org.boot.growup.common.constant;

public enum ExchangeStatus {
    PRE_EXCHANGE, // 교환 접수
    RETRIEVED, // 택배사 측 상품회수
    RETURN_ARRIVAL, // 판매자 측 반품상품 도착
    INSPECTED, // 판매자 측 상품 검수 중
    EXCHANGING, // 교환 중
    COMPLETE // 교환 완료
}
