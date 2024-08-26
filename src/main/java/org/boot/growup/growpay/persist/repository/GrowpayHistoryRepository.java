package org.boot.growup.growpay.persist.repository;


import org.boot.growup.growpay.persist.entity.GrowpayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrowpayHistoryRepository extends JpaRepository<GrowpayHistory, Long> {
    List<GrowpayHistory> findByGrowpayId(Long growpayId);
}