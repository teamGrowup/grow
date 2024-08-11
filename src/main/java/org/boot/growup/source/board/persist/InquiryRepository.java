package org.boot.growup.source.board.persist;

import org.boot.growup.source.board.persist.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

  Page<Inquiry> findByCustomer(long id, Pageable pageable);
}
