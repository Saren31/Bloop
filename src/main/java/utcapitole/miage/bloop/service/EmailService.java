package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void envoyerMessageConfirmation(String to, String confirmationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmation d'inscription à Bloop");
        message.setText("Bonjour ! Cliquez ici pour confirmer votre compte : " + confirmationLink);
        mailSender.send(message);
    }

}
