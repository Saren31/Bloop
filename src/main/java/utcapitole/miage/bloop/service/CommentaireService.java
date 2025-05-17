package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Commentaire;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.CommentaireRepository;
import utcapitole.miage.bloop.repository.PostRepository;

import java.util.Date;
import java.util.List;

@Service
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentaireService(CommentaireRepository commentaireRepository, PostRepository postRepository) {
        this.commentaireRepository = commentaireRepository;
        this.postRepository = postRepository;
    }

    public Commentaire ajouterCommentaire(Long postId, String texte, Utilisateur utilisateur) {
        Post post = postRepository.findById(postId).orElseThrow();
        Commentaire commentaire = new Commentaire();
        commentaire.setTexte(texte);
        commentaire.setDateCommentaire(new Date());
        commentaire.setUtilisateur(utilisateur);
        commentaire.setPost(post);
        return commentaireRepository.save(commentaire);
    }

    public List<Commentaire> getCommentairesParPost(Long postId) {
        return commentaireRepository.findByPostIdPost(postId);
    }
}