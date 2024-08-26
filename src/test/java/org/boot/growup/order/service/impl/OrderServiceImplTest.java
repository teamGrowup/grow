package org.boot.growup.order.service.impl;

import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.persist.repository.CustomerRepository;
import org.boot.growup.order.dto.OrderDTO;
import org.boot.growup.order.persist.entity.Order;
import org.boot.growup.order.persist.repository.OrderRepository;
import org.boot.growup.product.persist.entity.ProductOption;
import org.boot.growup.product.persist.repository.ProductOptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderServiceImplTest {

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductOptionRepository productOptionRepository;

    @Test
    void processNormalOrderTest_Defualt_Success() {
        // given
        OrderDTO orderDTO = OrderDTO.builder()
                .message("문 앞에 둬주세요.")
                .receiverAddress("용인시 기흥구")
                .receiverName("정태승")
                .receiverPhone("010-1234-5678")
                .receiverPostCode("12345")
                .build();
        Customer customer = customerRepository.findById(1L).get();

        ProductOption po1 = productOptionRepository.findById(1L).get();
        ProductOption po2 = productOptionRepository.findById(2L).get();
        ProductOption po3 = productOptionRepository.findById(3L).get();
        ProductOption po4 = productOptionRepository.findById(4L).get();

        System.out.println(po1.getOptionPrice());
        int expectedTotalPrice =
                po1.getOptionPrice() * 2 + po1.getProduct().getDeliveryFee()
                + po2.getOptionPrice() * 3 + po2.getProduct().getDeliveryFee()
                + po3.getOptionPrice() * 4 + po3.getProduct().getDeliveryFee()
                + po4.getOptionPrice() * 2 + po4.getProduct().getDeliveryFee();

        Map<ProductOption, Integer> productOptionCountMap = new HashMap<>();
        productOptionCountMap.put(po1, 2);
        productOptionCountMap.put(po2, 3);
        productOptionCountMap.put(po3, 4);
        productOptionCountMap.put(po4, 2);

        // when
        Order order = orderService.processNormalOrder(orderDTO, productOptionCountMap, customer);

        // then
        assertNotNull(order);
        assertNotNull(order.getMerchantUid());
        assertEquals(orderDTO.getMessage(), order.getMessage());
        assertEquals(orderDTO.getReceiverAddress(), order.getReceiverAddress());
        assertEquals(orderDTO.getReceiverName(), order.getReceiverName());
        assertEquals(orderDTO.getReceiverPhone(), order.getReceiverPhone());
        assertEquals(orderDTO.getReceiverPostCode(), order.getReceiverPostCode());
        assertEquals(1L, order.getId());
        assertEquals(order.getTotalPrice(), expectedTotalPrice);

    }

    @Test
    void completeOrderTest_Defualt_Success() {
        // given

        // when

        // then
    }
}