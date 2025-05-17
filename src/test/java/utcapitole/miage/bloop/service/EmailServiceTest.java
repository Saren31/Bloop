package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final EmailService emailService = new EmailService(mailSender);

    @Test
    void testEnvoyerMessageConfirmation() {
        String to = "test@example.com";
        String link = "http://test/confirm";

        emailService.envoyerMessageConfirmation(to, link);

        verify(mailSender).send(argThat((SimpleMailMessage message) ->
                message.getTo()[0].equals(to)
                        && message.getSubject().contains("Confirmation")
                        && message.getText().contains(link)
        ));
    }
}