package soma.edupiuser.account.exception;

import soma.edupiuser.web.exception.BaseException;
import soma.edupiuser.web.exception.ErrorEnum;

public class MetaServerException extends BaseException {

    public MetaServerException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}
