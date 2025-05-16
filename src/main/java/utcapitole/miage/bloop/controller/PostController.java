package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;
import utcapitole.miage.bloop.service.PostService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;


    @GetMapping("/mockLogin")
    public String mockLogin(HttpSession session) {
        Utilisateur utilisateur = utilisateurRepository.findById(1L).orElse(null);
        session.setAttribute("utilisateur", utilisateur);
        return "creerPost";
    }


    @GetMapping("/creer")
    public String afficherFormulaire(Model model) {
        model.addAttribute("post", new Post());
        return "creerPost";
    }

    @PostMapping("/creer")
    public String creerPost(@ModelAttribute Post post,
                            @RequestParam("imageFile") MultipartFile image,
                            HttpSession session, Model model) throws IOException {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "login"; // rediriger vers login si pas connecté
        }

        post.setUtilisateur(utilisateur);
        post.setDatePost(new Date());

        if (!image.isEmpty()) {
            post.setImagePost(image.getBytes());
        }

        postService.creerPost(post);
        model.addAttribute("message", "Post créé avec succès !");

        // Récupérer les posts de l'utilisateur pour afficher sur profil
        List<Post> posts = postService.getPostsByUtilisateurId(utilisateur.getIdUser());
        model.addAttribute("posts", posts);
        model.addAttribute("utilisateur", utilisateur);

        return "voirProfil";
    }

    // Pour afficher l'image d’un post
    @GetMapping("/image/{id}")
    @ResponseBody
    public byte[] afficherImage(@PathVariable Long id) {
        Post post = postService.getPostParId(id);
        return post != null ? post.getImagePost() : null;
    }
}
