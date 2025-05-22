package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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


    @Autowired
    public AccueilController(EvenementService evenementService,
                             GroupeService groupeService,PostService postService) {
        this.evenementService = evenementService;
        this.groupeService = groupeService;
        this.postService = postService;
    }
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ReactionService reactionService;


    @GetMapping
    public String accueil(Model model) {
        // récupère tous les événements et tous les groupes
        model.addAttribute("allEvents",  evenementService.getAllEvents());
        model.addAttribute("allGroups",  groupeService.getAllGroups());

        model.addAttribute("allPosts", postService.getAllPosts());



        return "accueil";
    }

}