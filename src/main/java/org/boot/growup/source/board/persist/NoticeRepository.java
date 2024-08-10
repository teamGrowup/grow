package org.boot.growup.source.board.persist;

import org.boot.growup.source.board.persist.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
  Page<Notice> findAll(Pageable page);
}
