package org.boot.growup.common.model;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.auth.model.dto.request.EmailCheckRequestDTO;

@Data
@Builder
public class EmailMessageDTO {
    private final String to;      // 수신자
    private final String subject; // 메일 제목

    public static EmailMessageDTO from(EmailCheckRequestDTO request) {
        return EmailMessageDTO.builder()
                .to(request.getEmail())
                .subject("[Grow Team] 이메일 인증 코드")
                .build();
    }
}
