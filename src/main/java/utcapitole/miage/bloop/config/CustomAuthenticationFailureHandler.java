package utcapitole.miage.bloop.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Gestionnaire personnalisé pour les échecs d'authentification.
 * Redirige l'utilisateur vers une URL spécifique selon la cause de l'échec,
 * notamment si le compte n'est pas activé (email non vérifié).
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /**
     * Méthode appelée lors d'un échec d'authentification.
     * Redirige vers la page de connexion avec un paramètre d'erreur,
     * et ajoute un indicateur si l'email n'est pas vérifié.
     *
     * @param request   la requête HTTP
     * @param response  la réponse HTTP
     * @param exception l'exception d'authentification rencontrée
     * @throws IOException en cas d'erreur d'entrée/sortie
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String redirectUrl = "/auth/login?error=true";

        // Si l'utilisateur n'a pas vérifié son email, on ajoute un paramètre spécifique
        if (exception instanceof DisabledException) {
            redirectUrl = "/auth/login?error=true&email_not_verified=true";
        }

        response.sendRedirect(redirectUrl);

    }
}