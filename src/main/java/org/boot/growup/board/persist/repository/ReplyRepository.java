package org.boot.growup.board.persist.repository;

import org.boot.growup.board.persist.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
