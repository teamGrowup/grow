package org.boot.growup.source.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.source.board.dto.request.PostReplyRequestDTO;
import org.boot.growup.source.board.persist.repository.InquiryRepository;
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
    private final InquiryRepository inquiryRepository;

    /**
     * [관리자] 문의 답변 등록
     */
    @Transactional
    @Override
    public Long postReply(PostReplyRequestDTO input, Long admin,Inquiry inquiry) {
        // DTO -> Entity
        Reply reply = Reply.of(input, admin, inquiry);

        // 답변 등록
        Long id = replyRepository.save(reply).getId();

        // 답변 등록 컬럼 true 로 변경 (inquiry 테이블)
        Inquiry beforeInquiry = inquiryRepository.findById(inquiry.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        beforeInquiry.completeReply();

        return id;
    }
}
