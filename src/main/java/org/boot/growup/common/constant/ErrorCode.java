package org.boot.growup.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 고정 코드 */
    SUCCESS(HttpStatus.OK, 200, true, "요청에 성공하였습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, false, "입력값을 확인해주세요."),
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, false, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404, false, "대상을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, false, "서버 내부에 오류가 발생했습니다."),

    /* jwt */
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 401, false, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 401, false, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, 401, false, "지원되지 않는 토큰입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, 401, false, "잘못된 형식의 토큰입니다."),
    EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, 401, false, "JWT 토큰이 비어 있습니다."),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, 401, false, "잘못된 JWT 서명입니다."),
    ILLEGAL_ARGUMENT_TOKEN(HttpStatus.UNAUTHORIZED, 401, false, "JWT 토큰 처리 중 잘못된 인수가 전달되었습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, false, "알 수 없는 서버 오류가 발생했습니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, 401, false, "접근이 거부되었습니다."),

    /* Email Verify */
    USER_EMAIL_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, false, "이메일 전송 중 오류가 발생했습니다."),
    EMAIL_WRONG_AUTH_CODE(HttpStatus.BAD_REQUEST, 400, false, "이메일 인증번호가 틀립니다."),

    /* Phone Number Verify */
    PHONE_WRONG_AUTH_CODE(HttpStatus.BAD_REQUEST, 400, false, "문자 인증번호가 틀립니다."),
    INVALID_PHONE_NUMBER(HttpStatus.FORBIDDEN, 403, false, "인증되지 않은 전화번호 입니다."),

    /* Oauth2.0 */
    NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, 500, false, "구글 액세스 토큰 요청에 실패했습니다."),
    NOT_FOUND_KAKAO_ACCESS_TOKEN_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR,500,false,"카카오 액세스 토큰 요청에 실패했습니다."),
    NOT_FOUND_NAVER_ACCESS_TOKEN_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR,500,false,"네이버 액세스 토큰 요청에 실패했습니다."),
    NEED_TO_GIVE_ADDITIONAL_INFORMATION(HttpStatus.CREATED,201,true,"소셜 로그인 초기 사용자입니다. 추가 정보를 입력해주세요."),

    /* Session 관련 오류 */
    SESSION_EXPIRED(HttpStatus.BAD_REQUEST, 400, false, "세션이 만료되었습니다. 다시 로그인 해주세요."),
    SESSION_NOT_FOUND(HttpStatus.BAD_REQUEST, 400, false, "세션 정보를 찾을 수 없습니다."),
    SESSION_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 500, false, "세션에 데이터를 저장하는 데 실패했습니다."),

    /* Validation */
    INVALID_VALUE(HttpStatus.BAD_REQUEST, 400, false, "잘못된 입력값입니다."),
    USER_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, 500, false, "유저를 찾지 못했습니다."),

    /* Product 관련 */
    PRDOUCT_NOT_APPROVED(HttpStatus.BAD_REQUEST, 404, false, "해당 상품은 승인되지 않았습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST,404, false, "상품을 찾을 수 없습니다."),
    PRODUCT_NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,404, false, "해당 상품명은 이미 존재합니다."),
    PRODUCT_BY_SELLER_NOT_FOUND(HttpStatus.BAD_REQUEST, 404, false, "해당 셀러ID의 상품은 존재하지 않습니다."),
    SUBCATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, 404, false, "유효하지 않은 서브 카테고리 ID입니다."),
    PRODUCT_LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, 404, false, "좋아요 정보를 찾을 수 없습니다."),
    PRODUCT_OPTION_NOT_ENOUGH_STOCK(HttpStatus.CONFLICT, 409, false, "해당 상품 옵션의 재고가 부족합니다."),
    PRODUCT_OPTIONS_SOME_NOT_FOUND(HttpStatus.BAD_REQUEST,404, false, "일부 상품 옵션을 찾을 수 없습니다."),

    /* Brand 관련 */
    BRAND_NAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,404, false, "해당 브랜드명은 이미 존재합니다."),
    BRAND_BY_SELLER_NOT_FOUND(HttpStatus.BAD_REQUEST, 404, false, "해당 셀러ID의 브랜드는 존재하지 않습니다."),
    BRAND_BY_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, 404, false, "해당 ID를 가진 BRAND는 존재하지 않습니다."),

    /* Seller 관련 */
    SELLER_NOT_FOUND(HttpStatus.BAD_REQUEST,404, false, "해당 판매자를 찾을 수 없습니다."),

    /* CUSTOMER 관련 */
    CUSTOMER_NOT_FOUND(HttpStatus.BAD_REQUEST,404, false, "해당 소비자를 찾을 수 없습니다."),

    /* Admin 관련 */
    ADMIN_NOT_FOUND(HttpStatus.BAD_REQUEST,404, false, "해당 관리자를 찾을 수 없습니다."),

    /* Board 관련 */
    NOTICE_NOT_FOUND(HttpStatus.BAD_REQUEST, 404,false, "해당 공지사항을 찾을 수 없습니다."),
    INQUIRY_NOT_FOUND(HttpStatus.BAD_REQUEST, 404, false, "해당 문의를 찾을 수 없습니다."),

    /* Order 관련 */
    ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, 404, false, "해당 주문을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final Boolean isSuccess;
    private final String message;

    public Boolean isSuccess() {
        return this.isSuccess;
    }
}
