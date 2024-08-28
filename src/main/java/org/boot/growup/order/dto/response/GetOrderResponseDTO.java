package org.boot.growup.order.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.constant.PayMethod;
import org.boot.growup.order.persist.entity.Order;

import java.util.List;

@Data
@Builder
public class GetOrderResponseDTO {
    private String merchantUid;
    private int totalPrice;
    private PayMethod payMethod;
    private String message;
    private String receiverName;
    private String receiverPhone;
    private String receiverPostCode;
    private List<GetOrderItemResponseDTO> orderItems;

    public static GetOrderResponseDTO from(Order order) {
        return GetOrderResponseDTO.builder()
                .merchantUid(order.getMerchantUid())
                .totalPrice(order.getTotalPrice())
                .payMethod(order.getPayMethod())
                .message(order.getMessage())
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .receiverPostCode(order.getReceiverPostCode())
                .orderItems(order.getOrderItems().stream()
                        .map(oi -> GetOrderItemResponseDTO.of(oi, oi.getDelivery()))
                        .toList()
                )
                .build();
    }
}
