package org.boot.growup.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login/testorder")
public class OrderTestController {

    @GetMapping
    public String testMethod(){
        return "test-payv2.html";
    }

}
