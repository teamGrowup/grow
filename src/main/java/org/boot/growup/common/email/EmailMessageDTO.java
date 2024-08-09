package org.boot.growup.common.email;

import lombok.Builder;

@Builder
public record EmailMessageDTO(
    String to, // 수신자
    String subject // 메일 제목
) {}
