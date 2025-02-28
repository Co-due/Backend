package soma.edupiuser.oauth.models;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import soma.edupiuser.oauth.exception.OAuth2AuthenticationProcessingException;

@Slf4j
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (OAuth2Provider.GOOGLE.isEqualRegistrationId(registrationId)) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (OAuth2Provider.NAVER.isEqualRegistrationId(registrationId)) {
            return new NaverOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
        }
    }
}