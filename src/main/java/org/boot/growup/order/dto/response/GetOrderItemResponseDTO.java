package org.boot.growup.order.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.constant.OrderStatus;
import org.boot.growup.order.persist.entity.OrderItem;

@Data
@Builder
public class GetOrderItemResponseDTO {
    private Long id;
    private int count;
    private OrderStatus orderStatus;
    private int deliveryFee;
    private String productName;
    private String productOptionName;
    private int productOptionPrice;


    public static GetOrderItemResponseDTO from(OrderItem orderItem) {
        return GetOrderItemResponseDTO.builder()
                .id(orderItem.getId())
                .count(orderItem.getCount())
                .orderStatus(orderItem.getOrderStatus())
                .deliveryFee(orderItem.getDeliveryFee())
                .productName(orderItem.getProductName())
                .productOptionName(orderItem.getProductOptionName())
                .productOptionPrice(orderItem.getProductOptionPrice())
                .build();
    }
}
