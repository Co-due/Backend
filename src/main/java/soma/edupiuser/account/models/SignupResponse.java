package soma.edupiuser.account.models;

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
