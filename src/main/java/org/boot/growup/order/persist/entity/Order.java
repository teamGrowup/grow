package org.boot.growup.order.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.constant.PayMethod;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.order.dto.OrderDTO;

import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Getter
@Builder
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 25)
    private String name;

    @Column(nullable = false, length = 18)
    private String merchantUid;

    @Column(nullable = false)
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayMethod payMethod;

    @Column(length = 50)
    private String message;

    @Column(nullable = false, length = 10)
    private String receiverName;

    @Column(nullable = false, length = 13)
    private String receiverPhone;

    @Column(nullable = false, length = 100)
    private String receiverAddress;

    @Column(nullable = false, length = 5)
    private String receiverPostCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade  = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    public static Order of(OrderDTO orderDTO, Customer customer, PayMethod payMethod) {
        return Order.builder()
                .payMethod(payMethod)
                .customer(customer)
                .message(orderDTO.getMessage())
                .totalPrice(0)
                .receiverPostCode(orderDTO.getReceiverPostCode())
                .receiverName(orderDTO.getReceiverName())
                .receiverPhone(orderDTO.getReceiverPhone())
                .receiverAddress(orderDTO.getReceiverAddress())
                .build();
    }

    public void designateMerchantUid(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());

        // 무작위 10자리 숫자 생성
        Random random = new Random();
        long randomNum = 1000000000L + (long)(random.nextDouble() * 9000000000L);  // 10자리 숫자 생성

        // YYYYMMDD + 무작위 10자리 숫자로 설정.
        this.merchantUid = (dateStr + randomNum);
    }

    public void increaseTotalPrice(int orderItemPrice){
        this.totalPrice += orderItemPrice;
    }

    public void designateName() {
        if (orderItems.isEmpty()) {
            throw new BaseException(ErrorCode.ORDER_ITEM_NOT_FOUND);
        }

        int itemCount = orderItems.size();
        String firstProductName = orderItems.get(0).getProductName();

        this.name = String.format("%s 외 %d건", firstProductName, itemCount);
    }
}
