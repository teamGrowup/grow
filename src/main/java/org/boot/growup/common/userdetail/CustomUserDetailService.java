package org.boot.growup.common.userdetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.enumerate.Provider;
import org.boot.growup.source.admin.persist.entity.Admin;
import org.boot.growup.source.admin.persist.repository.AdminRepository;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.customer.persist.repository.CustomerRepository;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.boot.growup.source.seller.persist.repository.SellerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final AdminRepository adminRepository;

    public UserDetails loadUserByUsernameAndProvider(String username, Provider provider) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmailAndProvider(username, provider)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        CustomUserDetails userDetails = customer.toUserDetails();
        log.info("구매자 권한: {}", userDetails.getAuthorities());
        return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Seller seller = sellerRepository.findByCpEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        CustomUserDetails userDetails = seller.toUserDetails();
        log.info("판매자 권한 {}", userDetails.getAuthorities());
        return userDetails;
    }

    public UserDetails loadUserByUid(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUid(username)
                .orElseThrow(() -> new UsernameNotFoundException("등록된 관리자가 아닙니다."));

        CustomUserDetails userDetails = admin.toUserDetails();
        log.info("관리자 권한: {}", userDetails.getAuthorities());
        return userDetails;
    }
}
