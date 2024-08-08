package org.boot.growup.source.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.Gender;
import org.boot.growup.common.Role;
import org.boot.growup.common.oauth2.Provider;
import org.boot.growup.source.customer.dto.request.CustomerEmailSignUpRequest;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.customer.persist.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    public void signUp(CustomerEmailSignUpRequest request) {
        /* 비밀번호 암호화 */
        String encodedPassword = encodingPassword(request);
        log.info("SignUp Method => before pw : {} | after store pw : {}"
                , request.password()
                , encodedPassword);
        /* 데이터 삽입 */
        Customer newCustomer = Customer.builder()
                .email(request.email())
                .password(encodedPassword)
                .phoneNumber(request.phoneNumber())
                .birthday(request.birthday())
                .gender(Gender.valueOf(request.gender().name()))
                .address(request.address())
                .nickname(request.nickname())
                .name(request.name())
                .provider(Provider.EMAIL)
                .role(Role.CUSTOMER)
                .build();
        customerRepository.save(newCustomer);
    }

    public String encodingPassword(CustomerEmailSignUpRequest request){
        return passwordEncoder.encode(request.password());
    }
}
