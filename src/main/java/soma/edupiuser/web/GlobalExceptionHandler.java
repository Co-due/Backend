package soma.edupiuser.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ErrorResponse> handleServerException(BaseException exception) {
        log.error("[{}] code={}, message={}", exception.getClass().getSimpleName(), exception.getErrorCode(),
            exception.getMessage());
        ErrorEnum errorEnum = exception.getErrorCode();

        return ResponseEntity
            .status(errorEnum.getHttpStatus())
            .body(new ErrorResponse(errorEnum.getCode(), errorEnum.getDetail()));
    }

    @ExceptionHandler(MetaServerException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MetaServerException exception) {
        log.error("[Meta Exception] code={}, message={}, detail message={}", exception.getErrorCode().getCode(),
            exception.getErrorCode().getDetail(),
            exception.getMessage());

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(ErrorEnum.TASK_FAIL.getCode(), ErrorEnum.TASK_FAIL.getDetail()));

    }

}
