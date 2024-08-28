package org.boot.growup.auth.model.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.auth.persist.entity.Customer;
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

    public static GetCustomerInfoResponseDTO from(Customer currentCustomer) {
        return GetCustomerInfoResponseDTO.builder()
                .email(currentCustomer.getEmail())
                .phoneNumber(currentCustomer.getPhoneNumber())
                .birthday(currentCustomer.getBirthday())
                .gender(currentCustomer.getGender())
                .name(currentCustomer.getName())
                .nickname(currentCustomer.getNickname())
                .isValidPhoneNumber(currentCustomer.isValidPhoneNumber())
                .isValidEmail(currentCustomer.isValidEmail())
                .isAgreeSendEmail(currentCustomer.isAgreeSendEmail())
                .isAgreeSendSms(currentCustomer.isAgreeSendSms())
                .build();
    }
}
