package soma.edupiuser.oauth.models;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import soma.edupiuser.oauth.exception.OAuth2AuthenticationProcessingException;

@Slf4j
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
        String accessToken,
        Map<String, Object> attributes) {
        log.info("OAuth2UserInfoFactory 도착");
        if (OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) {
            return new GoogleOAuth2UserInfo(accessToken, attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
        }
    }
}