package soma.edupi.user;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import soma.edupi.user.dto.response.ErrorResponse;
import soma.edupi.user.exception.DbValidException;
import soma.edupi.user.exception.InnerServerException;

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
        ErrorResponse errors = new ErrorResponse(ex.getMessage());

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errors);
    }

    @ExceptionHandler(InnerServerException.class)    // 내부 서버 에러에 대한 예외 처리
    public ResponseEntity<ErrorResponse> handleValidationExceptions() {
        // 사용자 에러로 변경
        ErrorResponse error = new ErrorResponse("잘못된 요청입니다. 다시 시도 해주세요.");

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }
}
