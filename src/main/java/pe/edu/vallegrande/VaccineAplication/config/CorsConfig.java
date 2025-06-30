package pe.edu.vallegrande.VaccineAplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Configuration
public class CorsConfig {
    
    private static final List<String> STATIC_ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:4200",
            "https://ms-vaccineaplications.onrender.com"
    );

    private static final Pattern GITPOD_REGEX = Pattern.compile(
            "^https://4200-[a-z0-9\\-]+\\.ws-[a-z0-9]+\\.gitpod\\.io$"
    );

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Set allowed methods
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Set allowed headers
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // Allow credentials
        config.setAllowCredentials(true);
        
        // Set max age for preflight cache
        config.setMaxAge(3600L);
        
        // Create the configuration source
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
                String origin = exchange.getRequest().getHeaders().getOrigin();
                
                if (isAllowedOrigin(origin)) {
                    CorsConfiguration dynamicConfig = new CorsConfiguration();
                    dynamicConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                    dynamicConfig.setAllowedHeaders(Arrays.asList("*"));
                    dynamicConfig.setAllowCredentials(true);
                    dynamicConfig.setMaxAge(3600L);
                    dynamicConfig.setAllowedOrigins(Arrays.asList(origin));
                    return dynamicConfig;
                }
                
                // Fallback configuration for unmatched origins
                return null;
            }
        };
        
        // Register the configuration for all paths
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }

    private boolean isAllowedOrigin(String origin) {
        if (origin == null) return false;
        return STATIC_ALLOWED_ORIGINS.contains(origin) || GITPOD_REGEX.matcher(origin).matches();
    }
}
