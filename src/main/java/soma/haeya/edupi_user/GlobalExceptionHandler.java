package soma.haeya.edupi_user;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import soma.haeya.edupi_user.dto.response.ErrorResponse;
import soma.haeya.edupi_user.exception.DbValidException;
import soma.haeya.edupi_user.exception.ErrorCode;
import soma.haeya.edupi_user.exception.UnexpectedServerException;

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

    @ExceptionHandler(DbValidException.class)    // DB 로직 실패에 대한 예외 처리
    public ResponseEntity<ErrorResponse> handleValidationExceptions(DbValidException ex) {
        ErrorResponse errors = new ErrorResponse(ex.getMessage(), ErrorCode.Alert);

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errors);
    }

    @ExceptionHandler(UnexpectedServerException.class)    // 예상치 못한 예외에 대한 처리
    public ResponseEntity<ErrorResponse> handleValidationExceptions(UnexpectedServerException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), ErrorCode.Alert);

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }
}
