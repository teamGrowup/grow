package org.boot.growup.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ValidationErrorResponse {
  private int code;
  private boolean isSuccess;
  private List<Map<String, String>> message;
}
