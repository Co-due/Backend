package soma.haeya.edupi_user.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("중복 이메일입니다.");
    }
}
