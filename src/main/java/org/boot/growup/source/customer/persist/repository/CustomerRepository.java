package org.boot.growup.source.customer.persist.repository;

import org.boot.growup.common.enumerate.Provider;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmailAndProvider(String email, Provider provider);
    Optional<Customer> findByEmail(String email);
}
