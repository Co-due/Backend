package soma.edupiuser.web.exception;

public class AccountException extends BaseException {

    public AccountException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}

