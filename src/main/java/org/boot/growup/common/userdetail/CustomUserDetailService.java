package org.boot.growup.common.userdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.customer.persist.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username).orElse(null);
        if (customer != null) {
            CustomUserDetails userDetails = customer.toUserDetails();
            log.info("구매자 권한 {}", userDetails.getAuthorities());
            return userDetails;
        }

        throw new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.");
    }
}
