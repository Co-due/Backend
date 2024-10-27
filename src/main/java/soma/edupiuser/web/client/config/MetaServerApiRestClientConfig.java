package soma.edupiuser.web.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
<<<<<<< HEAD:src/main/java/soma/edupiuser/account/client/config/MetaServerApiRestClientConfig.java
import soma.edupiuser.account.client.MetaServerApiClient;
=======
import soma.edupiuser.web.client.MetaServerApiClient;
>>>>>>> 8d65b35 ([#48]fiix: handler 함수 분리):src/main/java/soma/edupiuser/web/client/config/MetaServerApiRestClientConfig.java

@Configuration
public class MetaServerApiRestClientConfig {

    @Bean
<<<<<<< HEAD:src/main/java/soma/edupiuser/account/client/config/MetaServerApiRestClientConfig.java
    public MetaServerApiClient meatServerApiClient(
        @Value("${server-url.meta-server}") String metaUrl) {
=======
    public MetaServerApiClient dbServerApiClient(
        @Value("${server-url.db-server}") String dbUrl) {
>>>>>>> 8d65b35 ([#48]fiix: handler 함수 분리):src/main/java/soma/edupiuser/web/client/config/MetaServerApiRestClientConfig.java
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
