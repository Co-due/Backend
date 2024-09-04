package soma.haeya.edupi_user.dto.response;

import lombok.Getter;

@Getter
public class ErrorResponse {

    String message;
    int errorCode;

    public ErrorResponse(String message) {
        this.message = message;
    }
    
    public ErrorResponse(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
