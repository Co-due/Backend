package soma.haeya.edupi_user.dto.response;

import lombok.Getter;

@Getter
public class SignupResponse {

    String message;

    public SignupResponse() {
    }

    public SignupResponse(String message) {
        this.message = message;
    }
}
