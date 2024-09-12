package soma.edupi.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupResponse {

    String message;

    public SignupResponse(String message) {
        this.message = message;
    }
}
