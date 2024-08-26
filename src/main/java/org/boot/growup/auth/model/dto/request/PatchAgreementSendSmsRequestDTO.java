package org.boot.growup.auth.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchAgreementSendSmsRequestDTO {
    private boolean agreementSendSms;
}
