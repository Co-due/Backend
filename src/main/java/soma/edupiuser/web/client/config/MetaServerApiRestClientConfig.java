package soma.edupiuser.web.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import soma.edupiuser.web.client.MetaServerApiClient;

@Configuration
public class MetaServerApiRestClientConfig {

    @Bean
    public MetaServerApiClient meatServerApiClient(
        @Value("${server-url.meta-server}") String metaUrl) {
        RestClient restClient = RestClient.builder()
            .baseUrl(metaUrl)
            .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
            .builderFor(adapter)
            .build();

        return factory.createClient(MetaServerApiClient.class);
    }

}
