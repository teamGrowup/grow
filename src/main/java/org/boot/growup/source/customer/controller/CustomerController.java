package org.boot.growup.source.customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.boot.growup.source.customer.dto.request.CustomerEmailSignUpRequest;
import org.boot.growup.source.customer.service.CustomerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/email/register")
    public void signUp(@Valid @RequestBody CustomerEmailSignUpRequest request) {
        customerService.signUp(request);
    }
}
