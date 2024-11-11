package soma.edupiuser.account.exception;

import java.util.Map;
import soma.edupiuser.web.exception.BaseException;
import soma.edupiuser.web.exception.ErrorEnum;

public class MetaServerException extends BaseException {

    public MetaServerException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public MetaServerException(ErrorEnum errorEnum, Map<String, Object> result) {
        super(errorEnum, result);
    }
}
