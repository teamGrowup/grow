package org.boot.growup.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.common.utils.Regex;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostPhoneNumberRequestDTO {
    @NotBlank(message = "전화번호는 필수 입력 값입니다")
    @Pattern(regexp = Regex.PHONE_NUMBER, message = "전화번호는 000-0000-0000 형식이어야 합니다")
    @Size(max = 13, message = "전화번호는 최대 13글자 입니다")
    private String phoneNumber;

    private Provider provider;
}
