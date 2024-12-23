//package ma.emsi.application.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//
//import java.util.Collections;
//
//@Configuration
//public class ApplicationConfig {
//
//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Intercepteur pour ajouter le token récupéré depuis la session
//        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
//            String token = getTokenFromSession();
//            if (token != null) {
//                request.getHeaders().setBearerAuth(token); // Ajouter le token à l'entête
//            }
//            return execution.execute(request, body);
//        };
//
//        restTemplate.setInterceptors(Collections.singletonList(authInterceptor));
//        return restTemplate;
//    }
//
//    private String getTokenFromSession() {
//        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            return authentication.getCredentials().toString();
//        }
//        return null;
//    }
//}
