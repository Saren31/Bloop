package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Reaction;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.ReactionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private PostService postService;


    public Reaction likerPost(Post post, Utilisateur utilisateur) {
        List<Reaction> existingReactions = reactionRepository.findByPostAndUtilisateur(post, utilisateur);

        if (!existingReactions.isEmpty()) {
            return existingReactions.get(0);
        } else {
            Reaction reaction = new Reaction();
            reaction.setPost(post);
            reaction.setUtilisateur(utilisateur);
            reaction.setLiked(true);
            Reaction savedReaction = reactionRepository.save(reaction);

            int likes = getNombreLikes(post);
            post.setNbLikes(likes);
            postService.save(post);

            return savedReaction;
        }
    }

    public void dislikerPost(Post post, Utilisateur utilisateur) {
        List<Reaction> existingReactions = reactionRepository.findByPostAndUtilisateur(post, utilisateur);
        if (!existingReactions.isEmpty()) {
            Reaction reaction = existingReactions.get(0);
            if (reaction.isLiked()) {
                reaction.setLiked(false);
                reactionRepository.save(reaction);
            }
        } else {
            Reaction reaction = new Reaction();
            reaction.setPost(post);
            reaction.setUtilisateur(utilisateur);
            reaction.setLiked(false);
            reactionRepository.save(reaction);
        }


        int likes = getNombreLikes(post);
        post.setNbLikes(likes);
        postService.save(post);
    }

    public void annulerReaction(Post post, Utilisateur utilisateur) {
        List<Reaction> existingReactions = reactionRepository.findByPostAndUtilisateur(post, utilisateur);
        existingReactions.forEach(reactionRepository::delete);

        int likes = getNombreLikes(post);
        post.setNbLikes(likes);
        postService.save(post);
    }

    public int getNombreLikes(Post post) {
        return reactionRepository.countByPostAndLiked(post, true);
    }

    public Optional<Reaction> getReaction(Post post, Utilisateur utilisateur) {
        List<Reaction> reactions = reactionRepository.findByPostAndUtilisateur(post, utilisateur);
        if (reactions.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(reactions.get(0));
        }
    }

    public Reaction saveReaction(Post post, Utilisateur utilisateur, boolean liked) {
        Optional<Reaction> optionalReaction = getReaction(post, utilisateur);

        Reaction reaction;
        if (optionalReaction.isPresent()) {
            reaction = optionalReaction.get();
            reaction.setLiked(liked);
        } else {
            reaction = new Reaction();
            reaction.setPost(post);
            reaction.setUtilisateur(utilisateur);
            reaction.setLiked(liked);
        }
        return reactionRepository.save(reaction);
    }
}
