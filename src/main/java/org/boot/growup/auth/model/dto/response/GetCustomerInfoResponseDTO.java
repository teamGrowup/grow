package org.boot.growup.auth.model.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.constant.Gender;

@Data
@Builder
public class GetCustomerInfoResponseDTO {
    private String email;
    private String phoneNumber;
    private String birthday;
    private Gender gender;
    private String name;
    private String nickname;
    private boolean isValidPhoneNumber;
    private boolean isValidEmail;
    private boolean isAgreeSendEmail;
    private boolean isAgreeSendSms;
}
