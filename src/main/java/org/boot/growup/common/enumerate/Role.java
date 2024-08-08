package org.boot.growup.common.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    CUSTOMER("CUSTOMER", "구매자"),
    SELLER("SELLER", "판매자"),
    USER("USER","회원"),
    ADMIN("ADMIN","관리자");

    private final String key;
    private final String title;
}
