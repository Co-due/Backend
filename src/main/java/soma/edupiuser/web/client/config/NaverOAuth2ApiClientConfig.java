package soma.edupiuser.web.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import soma.edupiuser.web.client.NaverOAuth2ApiClient;

@Configuration
public class NaverOAuth2ApiClientConfig {

    private static final String NAVER_API_URL = "https://nid.naver.com";

    @Bean
    public NaverOAuth2ApiClient googleOAuth2ApiClient() {
        RestClient restClient = RestClient.builder()
            .baseUrl(NAVER_API_URL)
            .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
            .builderFor(adapter)
            .build();

        return factory.createClient(NaverOAuth2ApiClient.class);
    }
}
