package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Commentaire;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.CommentaireRepository;
import utcapitole.miage.bloop.repository.jpa.PostRepository;

import java.util.Date;
import java.util.List;

/**
 * Service pour gérer les opérations liées aux commentaires.
 */
@Service
public class CommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final PostRepository postRepository;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param commentaireRepository Le repository pour gérer les entités Commentaire.
     * @param postRepository Le repository pour gérer les entités Post.
     */
    @Autowired
    public CommentaireService(CommentaireRepository commentaireRepository, PostRepository postRepository) {
        this.commentaireRepository = commentaireRepository;
        this.postRepository = postRepository;
    }

    /**
     * Ajoute un commentaire à un post spécifique.
     *
     * @param postId L'identifiant du post auquel ajouter le commentaire.
     * @param texte Le texte du commentaire.
     * @param utilisateur L'utilisateur qui ajoute le commentaire.
     * @return Le commentaire ajouté.
     */
    public Commentaire ajouterCommentaire(Long postId, String texte, Utilisateur utilisateur) {
        Post post = postRepository.findById(postId).orElseThrow();
        Commentaire commentaire = new Commentaire();
        commentaire.setTexte(texte);
        commentaire.setDateCommentaire(new Date());
        commentaire.setUtilisateur(utilisateur);
        commentaire.setPost(post);
        return commentaireRepository.save(commentaire);
    }

    /**
     * Récupère la liste des commentaires associés à un post spécifique.
     *
     * @param postId L'identifiant du post.
     * @return La liste des commentaires associés au post.
     */
    public List<Commentaire> getCommentairesParPost(Long postId) {
        return commentaireRepository.findByPostIdPost(postId);
    }
}