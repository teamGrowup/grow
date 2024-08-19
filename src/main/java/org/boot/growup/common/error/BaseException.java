package org.boot.growup.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int code;
    private final String errorMessage;

    public BaseException(ErrorCode errorcode) {
        super(errorcode.getMessage());
        this.errorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorcode.getMessage();
    }

    public BaseException(ErrorCode errorcode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorMessage;
    }

    public BaseException(ErrorCode errorcode, Exception cause) {
        super(errorcode.getMessage(), cause);
        this.errorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorcode.getMessage();
    }

    public BaseException(ErrorCode errorcode, String errorMessage, Exception cause) {
        super(errorMessage, cause);
        this.errorCode = errorcode;
        this.code = errorcode.getCode();
        this.errorMessage = errorMessage;
    }
}
