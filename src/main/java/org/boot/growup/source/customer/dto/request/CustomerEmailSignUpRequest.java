package org.boot.growup.source.customer.dto.request;

import org.boot.growup.common.Gender;

public record CustomerEmailSignUpRequest (
    String email,
    String password,
    String phoneNumber,
    String birthday,
    Gender gender,
    String address,
    String postCode,
    String name,
    String nickname
) {}
