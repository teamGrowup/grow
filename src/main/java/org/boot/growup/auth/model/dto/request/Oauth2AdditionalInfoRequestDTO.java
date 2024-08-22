package org.boot.growup.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.utils.Regex;
import org.boot.growup.common.constant.Gender;

@Data
@Builder
public class Oauth2AdditionalInfoRequestDTO {
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

    @Pattern(regexp = Regex.NAME, message = "이름은 한글과 영문만 포함하며, 최대 10글자입니다")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력 값입니다")
    @Pattern(regexp = Regex.NICKNAME, message = "닉네임은 한글, 영어, 숫자만 포함하며, 최대 20글자입니다")
    private String nickname;
}
