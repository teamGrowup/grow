package org.boot.growup.order.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.order.persist.entity.Order;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Data
@Builder
public class GetOrderHistoryResponseDTO {
    private String merchantUid;
    private int totalPrice;
    private String receiverName;
    private String receiverAddress;
    private String receiverPostCode;
    private String receiverPhone;
    private String orderName;
    private String message;
    private LocalDateTime createAt;

    public static Page<GetOrderHistoryResponseDTO> pageFrom(Page<Order> orders) {
        return orders.map(GetOrderHistoryResponseDTO::from);
    }

    private static GetOrderHistoryResponseDTO from(Order order) {
        return GetOrderHistoryResponseDTO.builder()
                .merchantUid(order.getMerchantUid())
                .totalPrice(order.getTotalPrice())
                .receiverName(order.getReceiverName())
                .receiverAddress(order.getReceiverAddress())
                .receiverPostCode(order.getReceiverPostCode())
                .receiverPhone(order.getReceiverPhone())
                .orderName(order.getName())
                .message(order.getMessage())
                .createAt(order.getCreatedAt())
                .build();
    }
}
