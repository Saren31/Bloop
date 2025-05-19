package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Reaction;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.ReactionRepository;

import java.util.Optional;

@Service
public class ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private PostService postService;

    public void toggleLike(Long idPost, Utilisateur utilisateur) {
        Optional<Reaction> existing = reactionRepository.findByPostIdPostAndUtilisateurIdUser(idPost, utilisateur.getIdUser());

        if (existing.isPresent()) {
            reactionRepository.delete(existing.get());
        } else {
            Reaction r = new Reaction();
            Post post = postService.getPostParId(idPost);
            r.setPost(post);
            r.setUtilisateur(utilisateur);
            r.setType("LIKE");
            r.setLiked(true);
            reactionRepository.save(r);
        }
    }

    public boolean isLikedBy(Post post, Utilisateur utilisateur) {
        return reactionRepository.findByPostAndUtilisateur(post, utilisateur)
                .map(Reaction::isLiked)
                .orElse(false);
    }

    public int countLikes(Post post) {
        return reactionRepository.countByPostAndType(post, "LIKE");
    }

    public void toggleDislike(Long idPost, Utilisateur utilisateur) {
        Post post = postService.getPostParId(idPost);

        Optional<Reaction> existing = reactionRepository.findByPostAndUtilisateur(post, utilisateur);
        if (existing.isPresent()) {
            if ("DISLIKE".equals(existing.get().getType())) {
                reactionRepository.delete(existing.get());
                return;
            } else {
                reactionRepository.delete(existing.get());
            }
        }

        Reaction r = new Reaction();
        r.setPost(post);
        r.setUtilisateur(utilisateur);
        r.setType("DISLIKE");
        reactionRepository.save(r);
    }

    public boolean isDislikedBy(Post post, Utilisateur utilisateur) {
        return reactionRepository.findByPostAndUtilisateur(post, utilisateur)
                .map(r -> "DISLIKE".equals(r.getType()))
                .orElse(false);
    }

    public int countDislikes(Post post) {
        return reactionRepository.countByPostAndType(post, "DISLIKE");
    }

}
