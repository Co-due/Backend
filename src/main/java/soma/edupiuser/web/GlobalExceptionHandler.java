package soma.edupiuser.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import soma.edupiuser.account.exception.DuplicatedEmailException;
import soma.edupiuser.account.exception.MetaServerException;
import soma.edupiuser.web.exception.AccountException;
import soma.edupiuser.web.exception.BaseException;
import soma.edupiuser.web.exception.ErrorEnum;
import soma.edupiuser.web.models.ErrorResponse;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AccountException.class, DuplicatedEmailException.class})
    public ResponseEntity<ErrorResponse> handleAccountException(BaseException exception) {
        log.info("[{}] code={}, message={}", exception.getClass().getSimpleName(), exception.getErrorCode().getCode(),
            exception.getMessage());
        ErrorEnum errorEnum = exception.getErrorCode();

        return ResponseEntity
            .status(errorEnum.getHttpStatus())
            .body(new ErrorResponse(errorEnum.getCode(), errorEnum.getDetail()));
    }

    @ExceptionHandler(MetaServerException.class)
    public ResponseEntity<ErrorResponse> handleMetaExceptions(BaseException exception) {
        log.error("[Meta Exception] code={}, message={}, result={}", exception.getErrorCode().getCode(),
            exception.getErrorCode().getDetail(),
            exception.getResult());

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(ErrorEnum.TASK_FAIL.getCode(), ErrorEnum.TASK_FAIL.getDetail()));
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingCookieExceptions(MissingRequestCookieException exception) {
        log.info("[MissingRequestCookieException] code={}, message={}", exception.getStatusCode(),
            exception.getMessage());

        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(new ErrorResponse(ErrorEnum.TOKEN_NOT_FOUND.getCode(), ErrorEnum.TOKEN_NOT_FOUND.getDetail()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        printErrorLog(exception);

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(ErrorEnum.TASK_FAIL.getCode(), ErrorEnum.TASK_FAIL.getDetail()));
    }

    private void printErrorLog(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        String className = stackTrace[0].getClassName();
        String methodName = stackTrace[0].getMethodName();

        String exceptionMessage = exception.getMessage();

        log.error("Exception occurred in class = {}, method = {}, message = {}, exception class = {}",
            className, methodName, exceptionMessage, exception.getClass().getCanonicalName());
    }
}
