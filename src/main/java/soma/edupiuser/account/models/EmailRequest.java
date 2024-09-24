package soma.edupiuser.account.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailRequest {

    private String email;

    public EmailRequest(String email) {
        this.email = email;
    }

}
