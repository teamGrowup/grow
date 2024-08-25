package org.boot.growup.growpay.persist.repository;

import org.boot.growup.growpay.persist.entity.Growpay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrowpayRepository extends JpaRepository<Growpay, Long> {
    // 추가적인 쿼리 메서드 정의 가능
}