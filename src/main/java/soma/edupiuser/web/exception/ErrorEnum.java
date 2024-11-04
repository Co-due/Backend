package soma.edupiuser.web.exception;

import org.springframework.http.HttpStatus;

public enum ErrorEnum {
    // 400
    TOKEN_EXPIRE(HttpStatus.BAD_REQUEST, "AC-400001", "The token has expired"),
    INVALID_ACCOUNT(HttpStatus.BAD_REQUEST, "AC-400002", "ID or password is invalid."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "AC-400003", "It's a duplicate email."),
    OAUTH2_EXCEPTION(HttpStatus.BAD_REQUEST, "AC-400004", "Social login failed."),
    TASK_FAIL(HttpStatus.BAD_REQUEST, "AC-400005", "Please try again in a moment"),
    RESOURCE_ACCESS_EXCEPTION(HttpStatus.BAD_REQUEST, "AC-400005",
        "Unable to get resources due to network issues. check the server is started"),

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
