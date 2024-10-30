package soma.edupiuser.web.exception;

import org.springframework.http.HttpStatus;

public enum ErrorEnum {
    // 400
    TOKEN_EXPIRE(HttpStatus.BAD_REQUEST, "AC-400001", "토큰이 만료되었습니다."),
    INVALID_ACCOUNT(HttpStatus.BAD_REQUEST, "AC-400002", "아이디 또는 비밀번호가 틀렸습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "AC-400003", "중복 이메일입니다."),

    META_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "AC-500001", "Meta server Exception"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String detail;

    ErrorEnum(HttpStatus httpStatus, String code, String details) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.detail = details;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }

}
