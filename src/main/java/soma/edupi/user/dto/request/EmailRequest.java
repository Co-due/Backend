package soma.edupi.user.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class EmailRequest {

    private final String email;

    @JsonCreator
    public EmailRequest(@JsonProperty("email") String email) {
        this.email = email;
    }

}
