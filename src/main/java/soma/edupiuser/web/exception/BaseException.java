package soma.edupiuser.web.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorEnum errorCode;

    public BaseException(ErrorEnum errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }
}
