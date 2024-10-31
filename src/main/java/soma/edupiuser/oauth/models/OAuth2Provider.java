package soma.edupiuser.oauth.models;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
    GOOGLE("google");

    private final String registrationId;

    public static boolean isOauth(String provider) {
        return Arrays.stream(values())
            .map(Enum::name)
            .anyMatch(name -> name.equalsIgnoreCase(provider));
    }
}