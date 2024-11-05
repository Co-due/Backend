package soma.edupiuser.account.exception;

import soma.edupiuser.web.exception.BaseException;
import soma.edupiuser.web.exception.ErrorEnum;

public class DuplicatedEmailException extends BaseException {

    public DuplicatedEmailException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

}
