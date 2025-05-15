package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

@Controller
public class EmailController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/confirm")
    public String confirmUser(@RequestParam("token") String token, Model model) {
        Utilisateur user = utilisateurRepository.findByTokenInscription(token);
        if (user != null) {
            user.setValiderInscription(true);
            user.setTokenInscription(null);
            utilisateurRepository.save(user);
            model.addAttribute("success", true);
        } else {
            model.addAttribute("success", false);
        }
        return "confirmer_profil";
    }
}
