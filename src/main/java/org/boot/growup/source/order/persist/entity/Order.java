package org.boot.growup.source.order.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.enumerate.PayMethod;
import org.boot.growup.source.customer.persist.entity.Customer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Entity
@Getter
@Builder
@Table(name = "order")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long id;

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

    public void designateMerchantUid(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());

        // 무작위 10자리 숫자 생성
        Random random = new Random();
        long randomNum = 1000000000L + (long)(random.nextDouble() * 9000000000L);  // 10자리 숫자 생성

        // YYYYMMDD + 무작위 10자리 숫자로 설정.
        this.merchantUid = (dateStr + randomNum);
    }
}
