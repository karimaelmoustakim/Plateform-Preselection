//package ma.emsi.service_authentification.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//
//@Component
//public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
//    private static final Logger logger = LoggerFactory.getLogger(CustomBasicAuthenticationEntryPoint.class);
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//        logger.info("Tentative d'accès non autorisée : " + request.getRequestURI());
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().println("HTTP Status 401 - " + authException.getMessage());
//    }
//
//
//    @Override
//    public void afterPropertiesSet() {
//        setRealmName("Preselection Platform");
//        super.afterPropertiesSet();
//    }
//}
//
