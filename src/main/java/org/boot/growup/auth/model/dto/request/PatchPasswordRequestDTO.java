package org.boot.growup.auth.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.boot.growup.common.utils.Regex;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchPasswordRequestDTO {
    @NotBlank(message = "비밀번호는 필수 입력 값입니다")
    @Pattern(regexp = Regex.PASSWORD, message = "비밀번호는 8 ~ 16자, 영문 대소문자, 숫자, @ 및 ! 특수문자를 포함해야 합니다.")
    private String password;
}
