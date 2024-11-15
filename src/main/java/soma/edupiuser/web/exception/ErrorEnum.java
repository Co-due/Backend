package soma.edupiuser.web.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorEnum {
    // 200
    TOKEN_EXPIRE(HttpStatus.ACCEPTED, "AC-202001", "The token has expired."),
    TOKEN_NOT_FOUND(HttpStatus.ACCEPTED, "AC-202002", "Token not found."),

    // 400,
    INVALID_ACCOUNT(HttpStatus.BAD_REQUEST, "AC-400002", "ID or password is invalid."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "AC-400003", "It's a duplicate email."),
    OAUTH2_EXCEPTION(HttpStatus.BAD_REQUEST, "AC-400004", "Social login failed."),
    TASK_FAIL(HttpStatus.BAD_REQUEST, "AC-400005", "Please try again in a moment."),
    RESOURCE_ACCESS_EXCEPTION(HttpStatus.BAD_REQUEST, "AC-400005",
        "Unable to get resources due to network issues. check the server is started."),
    RESPONSE_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "AC-400006",
        "Invalid error response format."),
    NOT_ACTIVATED_EXCEPTION(HttpStatus.BAD_REQUEST, "AC-400008", "Not Authenticated email user."),

    // 500
    META_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "AC-500001", "Meta server Exception."),
    NOT_MATCH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AC-500002", "No matching error found.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String detail;

    ErrorEnum(HttpStatus httpStatus, String code, String details) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.detail = details;
    }

}
