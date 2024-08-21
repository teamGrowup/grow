package org.boot.growup.source.customer.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.Regex;
import org.boot.growup.common.enumerate.Gender;

@Data
@Builder
public class CustomerSignUpRequestDTO {
    @NotBlank(message = "이메일은 필수 입력 값입니다")
    @Email(message = "이메일 형식으로 되어있어야 합니다")
    @Size(max = 300, message = "이메일은 최대 300글자 입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다")
    @Pattern(regexp = Regex.PASSWORD, message = "비밀번호는 8~16자, 영문 대소문자, 숫자, @ 및 ! 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "전화번호는 필수 입력 값입니다")
    @Pattern(regexp = Regex.PHONE_NUMBER, message = "전화번호는 000-0000-0000 형식이어야 합니다")
    @Size(max = 13, message = "전화번호는 최대 13글자 입니다")
    private String phoneNumber;

    @NotBlank(message = "생년월일은 필수 입력 값입니다")
    @Pattern(regexp = Regex.BIRTHDAY, message = "생년월일은 8자리여야 합니다(yyyymmdd)")
    @Size(max = 8, message = "생년월일은 최대 8글자 입니다")
    private String birthday;

    @NotNull(message = "성별은 필수 입력 값입니다")
    private Gender gender;

    @Size(max = 50, message = "주소는 최대 50글자 입니다")
    private String address;

    @Pattern(regexp = Regex.POST_CODE, message = "우편번호는 5자리 숫자여야 합니다")
    private String postCode;

    @Pattern(regexp = Regex.NAME, message = "이름은 한글과 영문만 포함하며, 최대 10글자입니다")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력 값입니다")
    @Pattern(regexp = Regex.NICKNAME, message = "닉네임은 한글, 영어, 숫자만 포함하며, 최대 20글자입니다")
    private String nickname;

    private boolean isValidPhoneNumber;

    private boolean isValidEmail;

    private boolean isAgreeSendEmail;

    private boolean isAgreeSendSms;
}

