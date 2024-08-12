package org.boot.growup.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /* 고정 코드 */
    SUCCESS(HttpStatus.OK, 200, true, "요청에 성공하였습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, false, "입력값을 확인해주세요."),
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, false, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404, false, "대상을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, false, "서버 내부에 오류가 발생했습니다."),

    /* jwt */
    TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, 401, false, "JWT Token이 존재하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 401, false, "유효하지 않은 JWT Token 입니다."),

    /* Email Send */
    USER_EMAIL_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, false, "이메일 전송 중 오류가 발생했습니다."),

    /* Oauth2.0 */
    NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, 500, false, "구글 액세스 토큰 요청에 실패했습니다."),
    NOT_FOUND_KAKAO_ACCESS_TOKEN_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR,500,false,"카카오 액세스 토큰 요청에 실패했습니다."),
    NOT_FOUND_NAVER_ACCESS_TOKEN_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR,500,false,"네이버 액세스 토큰 요청에 실패했습니다."),
    NEED_TO_GIVE_ADDITIONAL_INFORMATION(HttpStatus.CREATED,201,true,"구글 초기 사용자입니다. 추가 정보를 입력해주세요."),
    /* Session 관련 오류 */
    SESSION_EXPIRED(HttpStatus.BAD_REQUEST, 400, false, "세션이 만료되었습니다. 다시 로그인 해주세요."),
    SESSION_NOT_FOUND(HttpStatus.BAD_REQUEST, 400, false, "세션 정보를 찾을 수 없습니다."),
    SESSION_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 500, false, "세션에 데이터를 저장하는 데 실패했습니다."),
    /* Validation */
    INVALID_VALUE(HttpStatus.BAD_REQUEST, 400, false, "잘못된 입력값입니다."),
    USER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, 500, false, "유저를 찾지 못했습니다."),

    /* Brand 관련 */
    BRAND_NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,404, false, "해당 브랜드명은 이미 존재합니다."),
    BRAND_BY_SELLER_NOT_FOUND(HttpStatus.BAD_REQUEST, 404, false, "해당 셀러ID의 브랜드는 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final Boolean isSuccess;
    private final String message;

    public Boolean isSuccess() {
        return this.isSuccess;
    }
}
