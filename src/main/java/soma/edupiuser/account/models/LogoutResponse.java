package soma.edupiuser.account.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutResponse {

    private String isOauthUser;
    private String provider;


    public LogoutResponse(boolean isOauthUser, String provider) {
        this.isOauthUser = isOauthUser ? "true" : "false";
        this.provider = provider;
    }
}
