package org.boot.growup.auth.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.boot.growup.auth.service.EmailService;
import org.boot.growup.common.model.EmailMessageDTO;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public String sendMail(EmailMessageDTO emailMessage) throws MessagingException {
        String authCode = createCode();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
        mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목
        mimeMessageHelper.setText(setContext(authCode), true); // 메일 본문 내용, HTML 여부
        javaMailSender.send(mimeMessage);

        return authCode;
    }

    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("email", context);
    }
}
