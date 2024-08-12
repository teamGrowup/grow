package org.boot.growup.source.board.service;

import org.boot.growup.source.board.dto.request.PostReplyRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface ReplyService {
    public Long postReply(PostReplyRequestDTO input, Long admin, Long inquiry);
}
