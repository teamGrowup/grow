package org.boot.growup.order.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.order.dto.OrderDTO;
import org.boot.growup.order.dto.OrderItemDTO;

import java.util.List;

@Data
@Builder
public class ProcessNormalOrderRequestDTO {
    @NotNull(message = "주문 정보를 명시해야합니다.")
    private OrderDTO orderDTO;

    @NotEmpty(message = "적어도 1개 이상의 상품을 골라야합니다.")
    private List<OrderItemDTO> orderItemDTOs;
}