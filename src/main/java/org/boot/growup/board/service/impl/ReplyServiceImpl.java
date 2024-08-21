package org.boot.growup.board.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.persist.entity.Admin;
import org.boot.growup.board.dto.request.PostReplyRequestDTO;
import org.boot.growup.board.persist.entity.Inquiry;
import org.boot.growup.board.persist.entity.Reply;
import org.boot.growup.board.persist.repository.ReplyRepository;
import org.boot.growup.board.service.ReplyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;

    @Transactional
    @Override
    public Long postReply(PostReplyRequestDTO input, Admin admin, Inquiry inquiry) {
        Reply reply = Reply.of(input, admin);
        Long id = replyRepository.save(reply).getId();
        inquiry.completeReply(reply);
        return id;
    }
}
