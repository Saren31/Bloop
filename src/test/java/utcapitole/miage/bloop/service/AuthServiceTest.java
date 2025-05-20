package utcapitole.miage.bloop.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private final UtilisateurRepository utilisateurRepository = mock(UtilisateurRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final EmailService emailService = mock(EmailService.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);

    private final AuthService authService = new AuthService(utilisateurRepository, passwordEncoder, emailService);

    @Test
    void testEnregistrementValide() {
        Utilisateur user = new Utilisateur();
        user.setEmailUser("test@ut-capitole.fr");
        user.setMdpUser("pwd");

        Model model = new ConcurrentModel();

        when(utilisateurRepository.findByEmailUser("test@ut-capitole.fr")).thenReturn(null);
        when(passwordEncoder.encode("pwd")).thenReturn("hashed");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/auth/register_user"));
        when(request.getRequestURI()).thenReturn("/auth/register_user");
        when(request.getContextPath()).thenReturn("");

        String view = authService.enregistrerUtilisateur(user, request, model);

        verify(utilisateurRepository).save(user);
        verify(emailService).envoyerMessageConfirmation(eq("test@ut-capitole.fr"), contains("/confirm?token="));
        assertThat(view).isEqualTo("accueil");
    }

    @Test
    void testEmailInvalide() {
        Utilisateur user = new Utilisateur();
        user.setEmailUser("test@gmail.com");

        Model model = new ConcurrentModel();

        String view = authService.enregistrerUtilisateur(user, request, model);

        assertThat(view).isEqualTo("inscription");
        assertThat(model.getAttribute("error")).isEqualTo("L'adresse e-mail doit se terminer par @ut-capitole.fr");
    }

    @Test
    void testEmailDejaUtilise() {
        Utilisateur user = new Utilisateur();
        user.setEmailUser("test@ut-capitole.fr");

        Model model = new ConcurrentModel();
        when(utilisateurRepository.findByEmailUser("test@ut-capitole.fr")).thenReturn(new Utilisateur());

        String view = authService.enregistrerUtilisateur(user, request, model);

        assertThat(view).isEqualTo("inscription");
        assertThat(model.getAttribute("error")).isEqualTo("L'adresse e-mail est déjà utilisée");
    }
}
