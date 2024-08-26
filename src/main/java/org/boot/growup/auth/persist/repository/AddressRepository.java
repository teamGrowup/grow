package org.boot.growup.auth.persist.repository;

import org.boot.growup.auth.persist.entity.Address;
import org.boot.growup.auth.persist.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByCustomer(Customer customer);
}
