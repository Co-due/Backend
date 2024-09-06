package soma.haeya.edupi_user.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "JWT";
    private static final String SCHEME_TYPE = "bearer";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(apiInfo())
            .addSecurityItem(createSecurityRequirement())
            .components(createComponents());
    }

    private SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement().addList(SECURITY_SCHEME_NAME);
    }

    private Info apiInfo() {
        return new Info()
            .title("Edupi User API")
            .version("1.0.0");
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