package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import utcapitole.miage.bloop.service.*;
import org.springframework.ui.Model;

/**
 * Contrôleur pour gérer les requêtes liées à la page d'accueil.
 */
@Controller
@RequestMapping({"/", "/accueil"})
public class AccueilController {

    private final EvenementService evenementService;
    private final GroupeService groupeService;
    private final PostService postService;
    private final UtilisateurService utilisateurService;
    private final ReactionService reactionService;

    @Autowired
    public AccueilController(EvenementService evenementService,
                             GroupeService groupeService,
                             PostService postService,
                             UtilisateurService utilisateurService,
                             ReactionService reactionService) {
        this.evenementService = evenementService;
        this.groupeService = groupeService;
        this.postService = postService;
        this.utilisateurService = utilisateurService;
        this.reactionService = reactionService;
    }

    @GetMapping
    public String accueil(
            @RequestParam(value = "pseudo", required = false) String pseudo,
            Model model) {

        // ① Tous les posts, évènements et groupes
        model.addAttribute("allPosts",  postService.getAllPosts());
        model.addAttribute("allEvents", evenementService.getAllEvents());
        model.addAttribute("allGroups", groupeService.getAllGroups());



        // ② Si on a un pseudo en requête, on recherche
        if (pseudo != null && !pseudo.isBlank()) {
            model.addAttribute("pseudo", pseudo);
            model.addAttribute(
                    "resultats",
                    utilisateurService.rechercherParPseudo(pseudo)
            );
        }

        return "accueil";
    }
}
