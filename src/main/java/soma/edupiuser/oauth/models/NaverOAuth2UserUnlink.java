package soma.edupiuser.oauth.models;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class NaverOAuth2UserUnlink implements OAuth2UserUnlink {

    private static final String URL = "https://nid.naver.com/oauth2.0/token";

    private final RestTemplate restTemplate;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Override
    public void unlink(String accessToken) {

        String url = UriComponentsBuilder.fromHttpUrl(URL)
            .queryParam("service_provider", "NAVER")
            .queryParam("grant_type", "delete")
            .queryParam("client_id", clientId)
            .queryParam("client_secret", clientSecret)
            .queryParam("access_token", accessToken)
            .toUriString();

        restTemplate.getForObject(url, String.class);

    }

}