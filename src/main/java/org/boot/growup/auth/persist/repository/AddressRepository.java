package org.boot.growup.auth.persist.repository;

import org.boot.growup.auth.persist.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
