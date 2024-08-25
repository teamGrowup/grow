package org.boot.growup.growpay.persist.repository;

import org.boot.growup.growpay.persist.entity.Growpay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrowpayRepository extends JpaRepository<Growpay, Long> {
}