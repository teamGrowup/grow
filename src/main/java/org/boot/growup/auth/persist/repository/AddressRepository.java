package org.boot.growup.auth.persist.repository;

import org.boot.growup.auth.persist.entity.Address;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.common.constant.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE a.customer = :customer AND a.status =:status")
    List<Address> findAllByCustomer(Customer customer, Status status);

    Optional<Address> findByCustomerAndId(Customer customer, Long addressId);
}
