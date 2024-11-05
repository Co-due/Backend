package soma.edupiuser.oauth.models;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import soma.edupiuser.web.client.NaverOAuth2ApiClient;

@RequiredArgsConstructor
@Component
public class NaverOAuth2UserUnlink implements OAuth2UserUnlink {

    private final NaverOAuth2ApiClient naverOAuth2ApiClient;

    private final RestTemplate restTemplate;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Override
    public void unlink(String accessToken) {
        naverOAuth2ApiClient.unlink(
            "NAVER",               // service_provider
            "delete",              // grant_type
            clientId,             // client_id
            clientSecret,         // client_secret
            accessToken           // access_token
        );
    }

}