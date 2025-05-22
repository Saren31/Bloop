package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Reaction;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.ReactionRepository;

import java.util.Optional;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostService postService;

    @Autowired
    public ReactionService(ReactionRepository reactionRepository, PostService postService) {
        this.reactionRepository = reactionRepository;
        this.postService = postService;
    }

    public void toggleLike(Long idPost, Utilisateur utilisateur) {
        Optional<Reaction> existing = reactionRepository.findByPostIdPostAndUtilisateurIdUser(idPost, utilisateur.getIdUser());

        if (existing.isPresent()) {
            if ("LIKE".equals(existing.get().getType())) {
                reactionRepository.delete(existing.get());
            } else {
                reactionRepository.delete(existing.get());
                addLike(idPost, utilisateur);
            }
        } else {
            addLike(idPost, utilisateur);
        }
    }

    private void addLike(Long idPost, Utilisateur utilisateur) {
        Post post = postService.getPostParId(idPost);
        Reaction r = new Reaction();
        r.setPost(post);
        r.setUtilisateur(utilisateur);
        r.setType("LIKE");
        r.setLiked(true);
        reactionRepository.save(r);
    }

    public void toggleDislike(Long idPost, Utilisateur utilisateur) {
        Optional<Reaction> existing = reactionRepository.findByPostIdPostAndUtilisateurIdUser(idPost, utilisateur.getIdUser());

        if (existing.isPresent()) {
            if ("DISLIKE".equals(existing.get().getType())) {
                reactionRepository.delete(existing.get());
            } else {
                reactionRepository.delete(existing.get());
                addDislike(idPost, utilisateur);
            }
        } else {
            addDislike(idPost, utilisateur);
        }
    }

    private void addDislike(Long idPost, Utilisateur utilisateur) {
        Post post = postService.getPostParId(idPost);
        Reaction r = new Reaction();
        r.setPost(post);
        r.setUtilisateur(utilisateur);
        r.setType("DISLIKE");
        r.setLiked(false);
        reactionRepository.save(r);
    }

    public boolean isLikedBy(Post post, Utilisateur utilisateur) {
        return reactionRepository.findByPostAndUtilisateur(post, utilisateur)
                .map(Reaction::isLiked)
                .orElse(false);
    }

    public int countLikes(Post post) {
        return reactionRepository.countByPostAndType(post, "LIKE");
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