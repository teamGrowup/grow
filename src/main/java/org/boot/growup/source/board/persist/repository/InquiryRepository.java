package org.boot.growup.source.board.persist.repository;

import org.boot.growup.source.board.persist.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
  Page<Inquiry> findByCustomer_Id(Long id, Pageable pageable);
  Page<Inquiry> findByIsAnswered(Boolean isAnswered, Pageable pageable);
}
