package org.boot.growup.source.order.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.source.customer.service.CustomerService;
import org.boot.growup.source.order.dto.request.ProcessOrderRequestDTO;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderApplication {
    private final CustomerService customerService;
    private final ProductService productService;
//    private final OrderService orderService;

    @Transactional
    public String processOrder(ProcessOrderRequestDTO form) {

        // 1. order 객체 생성.
//        Product product = productService.getProductById(form.getOrderItemDTOs().get(0).getProductId());
        Product product2 = productService.getProductById2(form.getOrderItemDTOs().get(0).getProductId());
        log.info(product2.toString());
        log.info(product2.getProductOptions().get(0).getOptionName());



        // 1-0. orderItem 객체 생성.
        // 1-1. 이때, productId를 통해서 해당 상품 정보들을 가져와야함.
        // 1-2. 이떄, productOptionId를 통해서 해당 상품옵션 정보들을 가져와야함.
        // 1-3. 각각의 orderItem들은 prepaid임.
        // 1-4. 주문번호 생성.
        // save
        // return은 order객체의 주문번호


        return null;
    }
}
