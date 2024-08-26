package org.boot.growup.auth.persist.repository;

import org.boot.growup.common.constant.Provider;
import org.boot.growup.auth.persist.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmailAndProvider(String email, Provider provider);
    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhoneNumberAndIsValidPhoneNumberAndProvider(
                String phoneNumber, boolean isValidPhoneNumber, Provider provider);
}
