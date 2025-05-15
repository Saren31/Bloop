package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.PostRepository;

import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/creer")
    public String afficherFormulaire(Model model) {
        model.addAttribute("post", new Post());
        return "creerPost";
    }

    @PostMapping("/creer")
    public String creerPost(@RequestParam("textePost") String textePost,
                            @RequestParam("image") MultipartFile image,
                            HttpSession session) throws IOException {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/profil/voirProfil";
        }

        Post post = new Post();
        post.setTextePost(textePost);
        post.setDatePost(new Date());
        post.setUtilisateur(utilisateur);

        if (!image.isEmpty()) {
            post.setImagePost(image.getBytes());
        }

        postRepository.save(post);
        return "redirect:/post/confirmation";
    }

    @GetMapping("/confirmation")
    public String confirmation() {
        return "confirmationPost";
    }
}

