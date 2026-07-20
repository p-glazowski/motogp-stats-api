package com.pglazowski.motogpstatsapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI motoGpStatsApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("MotoGP Stats API")
                        .version("1.0.0")
                        .description("""
                                REST API for managing MotoGP circuits and statistics.
                                
                                Features:
                                • Retrieve track information
                                • Search by circuit or country
                                • Create, update and delete tracks
                                • Validation and error handling
                                """)
                        .contact(new Contact()
                                .name("Piotr Głazowski")
                                .email("piotr.glazowski976@gmail.com")
                                .url("https://github.com/p-glazowski"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}