package org.boot.growup.common;

public class Regex {
    /**
     * 비밀번호 정규식
     * 글자수 : 8 ~ 16
     * 조합 : 영문 + 숫자 + 특수문자 -> 필수
     * 특수문자 : @, !만 허용
     * 대소문자 제한 x
     */
    public static final String PASSWORD = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@!])[a-zA-Z\\d@!]{8,16}$";
    public static final String NICKNAME = "^[가-힣a-zA-Z0-9]{1,20}$"; // 20글자 이하, 한글/영문/숫자 허용
    public static final String NAME = "^[가-힣a-zA-Z]{1,10}$"; // 10글자 이하, 한글/영문 허용
    public static final String POST_CODE = "^\\d{5}$"; // 우편번호 코드 5자릿수
    public static final String PHONE_NUMBER = "^\\d{3}-\\d{4}-\\d{4}$"; // 전화번호 010-1234-1234 형식
    public static final String BIRTHDAY = "^\\d{8}$"; // 생년월일 YYYYMMDD 형식
    public static final String BUSINESS_REGISTRATION_NUMBER = "^\\d{3}-\\d{2}-\\d{5}$"; // 사업자 등록번호 019-02-00001 형식
}
