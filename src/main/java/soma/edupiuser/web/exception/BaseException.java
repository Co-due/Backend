package soma.edupiuser.web.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorEnum errorCode;
    private final Map<String, Object> result;

    public BaseException(ErrorEnum errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
        this.result = new HashMap<>();
    }

    public BaseException(ErrorEnum errorCode, Map<String, Object> result) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
        this.result = result;
    }
}
