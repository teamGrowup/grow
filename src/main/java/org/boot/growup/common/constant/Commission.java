package org.boot.growup.common.constant;

import java.math.BigDecimal;

public class Commission {
    public static final BigDecimal COMMISSION_RATE = new BigDecimal("0.094"); // 쇼핑몰 수수료 비율 9.4%
    private Commission() {}

    /* 쇼핑몰 수수료 연산 */
    public static int calculateCommission(int orderItemPrice) {
        return COMMISSION_RATE.multiply(new BigDecimal(orderItemPrice)).intValue();
    }

    /* 판매자 예상 정산금 연산 */
    public static int calculateNetProceed(int orderItemPrice) {
        return orderItemPrice - COMMISSION_RATE.multiply(new BigDecimal(orderItemPrice)).intValue();
    }
}
