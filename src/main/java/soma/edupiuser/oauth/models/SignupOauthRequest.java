package soma.edupiuser.oauth.models;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupOauthRequest {

    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    private String name;

    private String provider;

}
