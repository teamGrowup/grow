package org.boot.growup.source.board.persist;

import org.boot.growup.source.board.persist.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
