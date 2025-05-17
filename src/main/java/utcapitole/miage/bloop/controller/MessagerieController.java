package utcapitole.miage.bloop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessagerieController {

    @GetMapping("/messagerie")
    public String afficherMessagerie() {
        return "messagerie"; // affiche messagerie.html (Thymeleaf)
    }

    // ...autres méthodes pour API REST ou WebSocket
}
