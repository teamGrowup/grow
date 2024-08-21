package org.boot.growup.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.model.User;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.auth.persist.entity.Admin;
import org.boot.growup.auth.persist.repository.AdminRepository;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.persist.repository.CustomerRepository;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.auth.persist.repository.SellerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final AdminRepository adminRepository;

    public UserDetails loadUserByUsernameAndProvider(String username, Provider provider) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmailAndProvider(username, provider)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        User userDetails = customer.toUserDetails();
        log.info("구매자 권한: {}", userDetails.getAuthorities());
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Seller> seller = sellerRepository.findByCpEmail(username);

        if (seller.isPresent()) {
            User userDetails = seller.get().toUserDetails();
            log.info("판매자 권한 {}", userDetails.getAuthorities());
            return userDetails;
        }

        Optional<Admin> admin = adminRepository.findByEmail(username);

        if (admin.isPresent()) {
            User userDetails = admin.get().toUserDetails();
            log.info("관리자 권한: {}", userDetails.getAuthorities());
            return userDetails;
        }

        throw new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.");
    }
}
