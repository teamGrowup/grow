package org.boot.growup.auth.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAddressResponseDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private String postcode;
}
