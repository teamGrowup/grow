package org.boot.growup.common.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    VISITOR("ROLE_VISITOR", "방문자"),
    CUSTOMER("ROLE_CUSTOMER", "구매자"),
    SELLER("ROLE_SELLER", "판매자"),
    ADMIN("ROLE_ADMIN", "관리자");
    private final String key;
    private final String title;
}
