package org.boot.growup.source.board.service;

import org.boot.growup.source.admin.persist.entity.Admin;
import org.boot.growup.source.board.dto.request.PostReplyRequestDTO;
import org.boot.growup.source.board.persist.entity.Inquiry;
import org.springframework.stereotype.Service;

@Service
public interface ReplyService {
    /*
    문의 답변 등록
     */
    Long postReply(PostReplyRequestDTO input, Admin admin, Inquiry inquiry);
}
