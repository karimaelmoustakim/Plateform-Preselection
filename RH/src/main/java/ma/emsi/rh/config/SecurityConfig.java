package ma.emsi.rh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/rh/**").permitAll() // Authentification requise pour les endpoints RH
                        .anyRequest().permitAll() // Tout le reste est autorisé
                );
//                .httpBasic() // Activer l'authentification HTTP Basic pour les tests
//                .and()
//                .sessionManagement(session -> session
//                        .maximumSessions(1) // Une seule session par utilisateur
//                );
        return http.build();
    }


}
