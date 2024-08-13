package org.boot.growup.source.customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.Regex;
import org.boot.growup.common.enumerate.Gender;
@Data
@Builder
public class GoogleAdditionalInfoRequestDTO {
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
}
