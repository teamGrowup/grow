package org.boot.growup.source.board.persist.repository;

import org.boot.growup.source.board.persist.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
  Page<Notice> findAll(Pageable page);
}
