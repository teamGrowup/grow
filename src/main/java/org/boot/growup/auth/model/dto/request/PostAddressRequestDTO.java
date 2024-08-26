package org.boot.growup.auth.model.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.utils.Regex;

@Data
@Builder
public class PostAddressRequestDTO {
    @Pattern(regexp = Regex.NAME, message = "이름은 한글과 영문만 포함하며, 최대 10글자입니다")
    private String name;

    @Pattern(regexp = Regex.PHONE_NUMBER, message = "전화번호는 000-0000-0000 형식이어야 합니다")
    @Size(max = 13, message = "전화번호는 최대 13글자 입니다")
    private String phoneNumber;

    @Size(max = 50, message = "주소는 최대 50글자 입니다")
    private String address;

    @Pattern(regexp = Regex.POST_CODE, message = "우편번호는 5자리 숫자여야 합니다")
    private String postcode;
}
