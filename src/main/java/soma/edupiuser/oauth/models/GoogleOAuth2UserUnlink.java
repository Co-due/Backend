package soma.edupiuser.oauth.models;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import soma.edupiuser.web.client.GoogleOAuth2ApiClient;

@RequiredArgsConstructor
@Component
public class GoogleOAuth2UserUnlink implements OAuth2UserUnlink {

    private final GoogleOAuth2ApiClient googleOAuth2ApiClient;

    @Override
    public void unlink(String accessToken) {
        googleOAuth2ApiClient.unlink(accessToken);
    }
}