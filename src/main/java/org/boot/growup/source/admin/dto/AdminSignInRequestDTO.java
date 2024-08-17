package org.boot.growup.source.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminSignInRequestDTO {
    private String uid;
    private String password;
}
