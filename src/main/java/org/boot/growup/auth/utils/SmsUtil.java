package org.boot.growup.auth.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.boot.growup.common.model.SmsProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsUtil {
    private final SmsProperty smsProperty;
    private DefaultMessageService messageService;

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(smsProperty.getKey(), smsProperty.getSecret(), "https://api.coolsms.co.kr");
    }

    // 단일 메시지 발송 예제
    public SingleMessageSentResponse sendMessage(String to, String authCode) {
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(smsProperty.getFrom());
        message.setTo(to);
        message.setText("[Grow] 아래의 인증번호를 입력해주세요\n" + authCode);

        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
