package soma.edupiuser.oauth.models;

import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
    GOOGLE("google"),
    NAVER("naver");

    private final String registrationId;

    public static boolean isOauth(String provider) {
        return Arrays.stream(OAuth2Provider.values())
            .map(Enum::name)
            .anyMatch(name -> name.equalsIgnoreCase(provider));
    }

    public boolean isEqualRegistrationId(String registrationId) {
        return Objects.equals(this.registrationId, registrationId);
    }
}