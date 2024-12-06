package ma.emsi.service_authentification.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CANDIDATE"))) {
            response.sendRedirect("/candidate/dashboard"); // Espace candidat
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_RECRUITER"))) {
            response.sendRedirect("/recruiter/dashboard"); // Espace recruteur
        } else {
            response.sendRedirect("/error"); // Redirection par défaut si aucun rôle n'est trouvé
        }
    }
}
