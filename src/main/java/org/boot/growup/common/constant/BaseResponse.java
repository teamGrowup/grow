package org.boot.growup.common.constant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.boot.growup.common.error.ErrorCode;

import static org.boot.growup.common.error.ErrorCode.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "isSuccess", "message", "data"})
public class BaseResponse<T> {

    private final int code;
    private final Boolean isSuccess;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /* 성공 시 */
    public BaseResponse(T data) {
        this.code = SUCCESS.getCode(); // 성공 코드 : 200
        this.isSuccess = SUCCESS.isSuccess(); // 성공 Flag
        this.message = SUCCESS.getMessage(); // 성공 메세지
        this.data = data; // JSON 데이터
    }

    /* 예외발생 시 */
    public BaseResponse(BaseException e) {
        this.isSuccess = e.getErrorCode().isSuccess();
        this.code = e.getCode();
        this.message = e.getMessage();
    }

    public BaseResponse(ErrorCode e) {
        this.isSuccess = e.isSuccess();
        this.code = e.getCode();
        this.message = e.getMessage();
    }
}
