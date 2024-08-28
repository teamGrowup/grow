package org.boot.growup.auth.service;

import jakarta.mail.MessagingException;
import org.boot.growup.common.model.EmailMessageDTO;

public interface EmailService {
    /*
    이메일 보내기
     */
    String sendMail(EmailMessageDTO emailMessage) throws MessagingException;

    /*
    인증코드 생성
     */
    String createCode();

    /*
    thymeleaf를 통한 html 적용
     */
    String setContext(String code);
}
