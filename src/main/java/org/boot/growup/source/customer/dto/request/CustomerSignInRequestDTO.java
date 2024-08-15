package org.boot.growup.source.customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.Regex;

@Data
@Builder
public class CustomerSignInRequestDTO {
    @NotBlank(message = "이메일은 필수 입력 값입니다")
    @Email(message = "이메일 형식으로 되어있어야 합니다")
    @Size(max = 300, message = "이메일은 최대 300글자 입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다")
    @Pattern(regexp = Regex.PASSWORD, message = "비밀번호는 8~16자, 영문 대소문자, 숫자, @ 및 ! 특수문자를 포함해야 합니다.")
    private String password;
}

