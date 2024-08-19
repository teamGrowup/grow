package org.boot.growup.source.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.source.admin.persist.entity.Admin;
import org.boot.growup.source.board.dto.request.PostReplyRequestDTO;
import org.boot.growup.source.board.persist.repository.ReplyRepository;
import org.boot.growup.source.board.persist.entity.Inquiry;
import org.boot.growup.source.board.persist.entity.Reply;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
