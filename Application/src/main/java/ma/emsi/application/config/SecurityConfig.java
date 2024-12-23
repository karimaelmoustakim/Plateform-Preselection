//package ma.emsi.application.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.session.web.http.CookieSerializer;
//import org.springframework.session.web.http.DefaultCookieSerializer;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour les APIs
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**").permitAll() // Public
//                        .requestMatchers("/api/applications/my-applications").permitAll() // Authentifié
//                        .requestMatchers("/api/applications/**").permitAll() // Accès RH
//                )
//                .formLogin(form -> form.disable()) // Désactivez la redirection
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login?logout").permitAll()
//                );
////                .sessionManagement(session -> session
////                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Sessions persistantes
////                );
//
//        return http.build();
//    }
//
////    @Bean
////    public CookieSerializer cookieSerializer() {
////        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
////        serializer.setCookieName("SESSIONID");
////        serializer.setCookiePath("/");
////        serializer.setDomainNamePattern("localhost|127.0.0.1"); // Ajout pour les tests locaux
////        return serializer;
////    }
//}
