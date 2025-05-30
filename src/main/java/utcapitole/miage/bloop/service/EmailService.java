package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer l'envoi d'e-mails.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Constructeur pour injecter le service d'envoi d'e-mails.
     *
     * @param mailSender Le composant JavaMailSender utilisé pour envoyer les e-mails.
     */
    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envoie un e-mail de confirmation d'inscription à un utilisateur.
     *
     * @param to               L'adresse e-mail du destinataire.
     * @param confirmationLink Le lien de confirmation à inclure dans l'e-mail.
     */
    public void envoyerMessageConfirmation(String to, String confirmationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmation d'inscription à Bloop");
        message.setText("Bonjour ! \nCliquez ici pour confirmer votre compte : " + confirmationLink);
        mailSender.send(message);

    }

}