package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.PostService;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

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
            return "login";
        }

        post.setUtilisateur(utilisateur);
        post.setDatePost(new Date());

        if (!image.isEmpty()) {
            post.setImagePost(image.getBytes());
        }

        postService.creerPost(post);
        model.addAttribute("message", "Post créé avec succès !");
        return "monProfil";
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<Post> postOpt = PostService.getPostParId(id);
        if (postOpt.isPresent() && postOpt.get().getImagePost() != null) {
            byte[] image = postOpt.get().getImagePost();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
