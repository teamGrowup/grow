package org.boot.growup.source.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.source.board.dto.request.PostNoticeRequestDTO;
import org.boot.growup.source.board.dto.response.GetNoticeResponseDTO;
import org.boot.growup.source.board.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board/notice")
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;

  /**
   * 1. 공지사항 등록
   */
  @PostMapping
  public BaseResponse<String> postNotice(
      @Valid @RequestBody PostNoticeRequestDTO postNoticeRequestDTO) {

    if (postNoticeRequestDTO == null) {
      throw new BaseException(ErrorCode.BAD_REQUEST);
    }

    // 1. 수정한 관리자 정보 가져오기
    String admin = "admin";

    Long id = noticeService.postNotice(postNoticeRequestDTO, admin);

    return new BaseResponse<>("공지사항 등록 성공. 공지사항 ID : " + id.toString());
  }

  /**
   * 2. 공지사항 목록 조회
   */
  @GetMapping
  public BaseResponse<Page<GetNoticeResponseDTO>> getNotice(
      @RequestParam(value="page", defaultValue="0") int page) {

    Page<GetNoticeResponseDTO> result = noticeService.getNotice(page);

    return new BaseResponse<>(result);
  }

  /**
   * 3. 공지사항 수정
   */
  @PatchMapping("/{noticeId}")
  public BaseResponse<String> patchNotice(@PathVariable Long noticeId,
      @Valid @RequestBody PostNoticeRequestDTO postNoticeRequestDTO) {

    // 1. 수정한 관리자 정보 가져오기
    String admin = "admin";

    Long id = noticeService.updateNotice(noticeId, postNoticeRequestDTO, admin);

    return new BaseResponse<>("공지사항 수정 성공. 공지사항 ID : " + id.toString());
  }

  /**
   * 4. 공지사항 게시글 상세 조회
   */
  @GetMapping("/{noticeId}")
  public BaseResponse<GetNoticeResponseDTO> getNoticeDetail(@PathVariable Long noticeId) {

    GetNoticeResponseDTO result = noticeService.getNoticeDetail(noticeId);

    return new BaseResponse<>(result);
  }
}