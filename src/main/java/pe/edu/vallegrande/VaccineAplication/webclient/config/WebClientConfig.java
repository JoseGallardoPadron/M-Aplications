package pe.edu.vallegrande.VaccineAplication.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl("https://nph-ciclodevida.onrender.com/cicloVida")
            .defaultHeader("Content-Type", "application/json")
            .build();
    }
}
