package soma.edupi.user.dto.request;

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
