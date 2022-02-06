package common.exception;

import lombok.Getter;
import webserver.domain.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.getStatusCode(), "S_001", "올바르지 않은 요청 값입니다."),

    DUPLICATED_USER_ID(HttpStatus.CONFLICT.getStatusCode(), "U_001", "이미 존재하는 사용자 아이디입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST.getStatusCode(), "U_002", "올바르지 않은 비밀번호입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.getStatusCode(), "U_003", "해당 회원정보를 찾을 수 없습니다."),
    AUTHORIZATION_ERROR(403, "AU_004", "해당 요청의 권한이 없습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
