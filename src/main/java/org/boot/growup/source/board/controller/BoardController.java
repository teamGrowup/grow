package org.boot.growup.source.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.boot.growup.source.board.dto.PostNoticeRequestDTO;
import org.boot.growup.source.board.service.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

  private final NoticeService noticeService;

  /**
   * 1. 공지사항 등록
   */
  @PostMapping("/notice")
  public ResponseEntity<String> postNotice(@Valid @RequestBody PostNoticeRequestDTO postNoticeRequestDTO) {

    Long id = noticeService.postNotice(postNoticeRequestDTO);

    return ResponseEntity.ok("공지사항 등록 성공. 공지사항 ID : " + id.toString());
  }

}
