package sn.project.consultation.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Consultation Médicale")
                        .description("Documentation des services REST pour la gestion des consultations médicales")
                        .version("1.0.0"));
    }
}
