package org.boot.growup.order.dto;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.order.persist.entity.OrderItem;

@Data
@Builder
public class OrderItemCancelDTO {
    private OrderItem orderItem;
    private int cancelPrice;
    private int currentCancellableAmount;

}
