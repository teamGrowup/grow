package org.boot.growup.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ValidationErrorResponse {
    private int code;
    private Boolean isSuccess;
    private List<Map<String, String>> message;
}
