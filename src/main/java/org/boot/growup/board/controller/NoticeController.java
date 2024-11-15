package org.boot.growup.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.board.dto.request.PostNoticeRequestDTO;
import org.boot.growup.board.dto.response.GetNoticeResponseDTO;
import org.boot.growup.board.service.NoticeService;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.auth.persist.entity.Admin;
import org.boot.growup.auth.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeController {
  private final NoticeService noticeService;
  private final AdminService adminService;

  /**
   * 공지사항 등록
   * @param postNoticeRequestDTO 공지사항 등록 Form
   * @return String
   */
  @PostMapping("/admins/board/notice")
  public BaseResponse<String> postNotice(@Valid @RequestBody PostNoticeRequestDTO postNoticeRequestDTO) {
    Admin admin = adminService.getCurrentAdmin();
    Long id = noticeService.postNotice(postNoticeRequestDTO, admin);
    return new BaseResponse<>("공지사항 등록 성공. 공지사항 ID : " + id.toString());
  }

  /**
   * 공지사항 목록 조회
   * @param pageNo 페이지 번호
   * @return Page<GetNoticeResponseDTO>
   */
  @GetMapping("/customers/board/notice")
  public BaseResponse<Page<GetNoticeResponseDTO>> getNotice(@RequestParam(value="pageNo", defaultValue="0") int pageNo) {
    Page<GetNoticeResponseDTO> result = noticeService.getNotice(pageNo);
    return new BaseResponse<>(result);
  }

  /**
   * 공지사항 수정
   * @param noticeId 공지사항ID
   * @param postNoticeRequestDTO 공지사항 수정 객체
   * @return String
   */
  @PatchMapping("/admins/board/notice/{noticeId}")
  public BaseResponse<String> patchNotice(@PathVariable Long noticeId,
      @Valid @RequestBody PostNoticeRequestDTO postNoticeRequestDTO) {
    Admin admin = adminService.getCurrentAdmin();
    Long id = noticeService.patchNotice(noticeId, postNoticeRequestDTO, admin);
    return new BaseResponse<>("공지사항 수정 성공. 공지사항 ID : " + id.toString());
  }

  /**
   * 공지사항 게시글 상세 조회
   * @param noticeId 공지사항ID
   * @return GetNoticeResponseDTO
   */
  @GetMapping("/customers/board/notice/{noticeId}")
  public BaseResponse<GetNoticeResponseDTO> getNoticeDetail(@PathVariable Long noticeId) {
    GetNoticeResponseDTO result = noticeService.getNoticeDetail(noticeId);
    return new BaseResponse<>(result);
  }

  /**
   * 공지사항 게시글 삭제
   * @param noticeId 공지사항ID
   * @return Long
   */
  @DeleteMapping("/admins/board/notice/{noticeId}")
  public BaseResponse<Long> deleteNotice(@PathVariable Long noticeId) {
    return new BaseResponse<>(noticeService.deleteNotice(noticeId));
  }
}