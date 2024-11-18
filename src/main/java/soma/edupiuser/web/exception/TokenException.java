package soma.edupiuser.web.exception;

public class TokenException extends BaseException {

    public TokenException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}
