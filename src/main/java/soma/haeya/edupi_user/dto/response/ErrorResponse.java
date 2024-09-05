package soma.haeya.edupi_user.dto.response;

import lombok.Getter;

@Getter
public class ErrorResponse {

    String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
