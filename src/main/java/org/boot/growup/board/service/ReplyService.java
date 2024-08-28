package org.boot.growup.board.service;

import org.boot.growup.auth.persist.entity.Admin;
import org.boot.growup.board.persist.entity.Inquiry;
import org.boot.growup.board.dto.request.PostReplyRequestDTO;

public interface ReplyService {
    /*
    문의 답변 등록
     */
    Long postReply(PostReplyRequestDTO input, Admin admin, Inquiry inquiry);
}
