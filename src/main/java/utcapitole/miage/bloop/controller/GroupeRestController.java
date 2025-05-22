package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utcapitole.miage.bloop.dto.PostDTO;
import utcapitole.miage.bloop.service.PostService;

import java.util.List;

/**
 * Contrôleur REST pour gérer les opérations liées aux groupes.
 */
@RestController
@RequestMapping("/groupes")
public class GroupeRestController {

    private final PostService postService;

    /**
     * Constructeur pour injecter le service des posts.
     *
     * @param postService Service pour la gestion des posts.
     */
    @Autowired
    public GroupeRestController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Récupère la liste des posts associés à un groupe spécifique.
     *
     * @param id L'identifiant du groupe.
     * @return Une liste de DTOs représentant les posts du groupe.
     */
    @GetMapping("/{id}/posts")
    public List<PostDTO> getPostsByGroupe(@PathVariable Long id) {
        return postService.getPostsByGroupe(id);
    }
}