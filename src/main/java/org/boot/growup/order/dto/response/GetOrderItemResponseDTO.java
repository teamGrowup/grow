package org.boot.growup.order.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.constant.OrderStatus;
import org.boot.growup.order.persist.entity.Delivery;
import org.boot.growup.order.persist.entity.OrderItem;
import org.springframework.util.ObjectUtils;

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
    private int carrierCode;
    private String trackNumber;

    public static GetOrderItemResponseDTO of(OrderItem orderItem, Delivery delivery) {
        GetOrderItemResponseDTOBuilder builder = GetOrderItemResponseDTO.builder()
                                                .id(orderItem.getId())
                                                .count(orderItem.getCount())
                                                .orderStatus(orderItem.getOrderStatus())
                                                .deliveryFee(orderItem.getDeliveryFee())
                                                .productName(orderItem.getProductName())
                                                .productOptionName(orderItem.getProductOptionName())
                                                .productOptionPrice(orderItem.getProductOptionPrice());

        if (ObjectUtils.isEmpty(delivery)) {
            return builder.build();
        }

        return builder.carrierCode(delivery.getCarrierCode())
                        .trackNumber(delivery.getTrackingNumber())
                        .build();
    }
}
