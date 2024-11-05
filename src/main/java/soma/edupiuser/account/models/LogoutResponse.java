package soma.edupiuser.account.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutResponse {

    @JsonProperty("isOauthUser")
    private boolean isOauthUser;
    private String provider;


    public LogoutResponse(boolean isOauthUser, String provider) {
        this.isOauthUser = isOauthUser;
        this.provider = provider;
    }
}
