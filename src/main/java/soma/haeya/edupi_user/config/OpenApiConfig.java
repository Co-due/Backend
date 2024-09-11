package soma.haeya.edupi_user.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "JWT";
    private static final String SCHEME_TYPE = "bearer";

    @Bean
    public OpenAPI openAPI(
        @Value("${openapi.service.url}") String url,
        @Value("${openapi.service.title}") String title,
        @Value("${openapi.service.version}") String version) {
        return new OpenAPI()
            .servers(List.of(new Server().url(url)))
            .info(apiInfo(title, version))
            .addSecurityItem(createSecurityRequirement())
            .components(createComponents());
    }

    private SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement().addList(SECURITY_SCHEME_NAME);
    }

    private Info apiInfo(String title, String version) {
        return new Info()
            .title(title)
            .version(version);
    }

    private Components createComponents() {
        return new Components()
            .addSecuritySchemes(SECURITY_SCHEME_NAME,
                new SecurityScheme()
                    .name(SECURITY_SCHEME_NAME)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme(SCHEME_TYPE)    // JWT 토큰이 필요한 API 테스트를 위해 명시
                    .bearerFormat(SECURITY_SCHEME_NAME)
            );
    }
}