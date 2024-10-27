package soma.edupiuser.web;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import soma.edupiuser.account.exception.MetaServerException;
import soma.edupiuser.web.exception.AccountException;
import soma.edupiuser.web.exception.ErrorEnum;
import soma.edupiuser.web.models.ErrorResponse;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)    // @Valid 실패에 대한 예외 처리
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 오류 목록을 반복
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errors);
    }


    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handleServerException(AccountException exception) {
        ErrorEnum errorEnum = exception.getErrorCode();

        return ResponseEntity
            .status(errorEnum.getHttpStatus())
            .body(new ErrorResponse(errorEnum.getCode(), errorEnum.getDetail()));
    }

    @ExceptionHandler(MetaServerException.class)    // meta 로직 실패에 대한 예외 처리
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MetaServerException exception) {
        ErrorEnum errorEnum = exception.getErrorCode();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(errorEnum.getCode(), errorEnum.getDetail()));

    }

}
