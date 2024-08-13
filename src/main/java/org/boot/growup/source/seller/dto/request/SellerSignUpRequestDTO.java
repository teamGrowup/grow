package org.boot.growup.source.seller.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.Regex;
import org.boot.growup.common.enumerate.Gender;
import org.boot.growup.common.enumerate.Role;
import org.boot.growup.common.oauth2.Provider;
import org.boot.growup.common.userdetail.CustomUserDetails;
import org.boot.growup.source.customer.dto.request.CustomerSignUpRequestDTO;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.seller.persist.entity.Seller;

@Data
@Builder
public class SellerSignUpRequestDTO {
    @NotBlank(message = "회사 이메일은 필수 입력 값입니다")
    @Email(message = "이메일 형식으로 되어있어야 합니다")
    @Size(max = 300, message = "이메일은 최대 300글자 입니다")
    private String cpEmail;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다")
    @Pattern(regexp = Regex.PASSWORD, message = "비밀번호는 8~16자, 영문 대소문자, 숫자, @ 및 ! 특수문자를 포함해야 합니다.")
    private String cpPassword;

    @NotBlank(message = "전화번호는 필수 입력 값입니다")
    @Pattern(regexp = Regex.PHONE_NUMBER, message = "전화번호는 000-0000-0000 형식이어야 합니다")
    @Size(max = 13, message = "전화번호는 최대 13글자 입니다")
    private String phoneNumber;

    @NotBlank(message = "대표자 이름은 필수 입력 값입니다")
    @Pattern(regexp = Regex.NAME, message = "이름은 한글과 영문만 포함하며, 최대 10글자입니다")
    private String epName;

    @NotNull(message = "사업자 등록번호는 필수 입력 값입니다")
    @Pattern(regexp = Regex.BUSINESS_REGISTRATION_NUMBER, message = "사업자 등록번호는 000-00-00000 형식이어야 합니다")
    @Size(max = 12, message = "사업자 등록번호는 최대 12글자 입니다")
    private String cpCode;

    @Size(max = 50, message = "상호(법인)명은 최대 50글자 입니다")
    private String cpName;

    @Size(max = 100, message = "회사 주소는 최대 100글자 입니다")
    private String cpAddress;
}
