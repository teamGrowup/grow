package org.boot.growup.order.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.order.persist.entity.Order;

@Data
@Builder
public class ProcessNormalOrderResponseDTO {
    private String merchantUid;
    private String orderName;
    private int totalPrice;
    private String currency;
    private String payMethod;
    private String fullName;
    private String phoneNumber;
    private String email;

    public static ProcessNormalOrderResponseDTO of(Order order, Customer customer) {
        return ProcessNormalOrderResponseDTO.builder()
                .merchantUid(order.getMerchantUid())
                .orderName(order.getName())
                .totalPrice(order.getTotalPrice())
                .currency("CURRENCY_KRW")
                .payMethod("CARD")
                .fullName(customer.getName())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .build();
    }
}
