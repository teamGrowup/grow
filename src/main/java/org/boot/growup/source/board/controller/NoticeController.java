package org.boot.growup.source.board.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.source.board.dto.request.PostNoticeRequestDTO;
import org.boot.growup.source.board.dto.response.GetNoticeResponseDTO;
import org.boot.growup.source.board.service.NoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;

  /**
   * 1. 공지사항 등록
   */
  @PostMapping("/notice")
  public BaseResponse<String> postNotice(@Valid @RequestBody PostNoticeRequestDTO postNoticeRequestDTO) {

    if (postNoticeRequestDTO == null) {
      throw new BaseException(ErrorCode.BAD_REQUEST);
    }

    Long id = noticeService.postNotice(postNoticeRequestDTO);

    return new BaseResponse<>("공지사항 등록 성공. 공지사항 ID : " + id.toString());
  }

  /**
   * 2. 공지사항 전체 조회
   */
  @GetMapping("/notice")
  public BaseResponse<List<GetNoticeResponseDTO>> getNotice() {

    List<GetNoticeResponseDTO> result = noticeService.getNotice();

    return new BaseResponse<>(result);
  }

}