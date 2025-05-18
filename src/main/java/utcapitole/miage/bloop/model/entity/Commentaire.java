package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Entité représentant un commentaire dans le système.
 * Un commentaire est associé à un utilisateur et à un post.
 */
@Entity
public class Commentaire {

    /**
     * Identifiant unique du commentaire.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Texte du commentaire.
     */
    private String texte;

    /**
     * Date et heure de création du commentaire.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCommentaire;

    /**
     * Utilisateur ayant créé le commentaire.
     */
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    /**
     * Post auquel le commentaire est associé.
     */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * Récupère l'identifiant du commentaire.
     *
     * @return L'identifiant du commentaire.
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant du commentaire.
     *
     * @param id L'identifiant à définir.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupère le texte du commentaire.
     *
     * @return Le texte du commentaire.
     */
    public String getTexte() {
        return texte;
    }

    /**
     * Définit le texte du commentaire.
     *
     * @param texte Le texte à définir.
     */
    public void setTexte(String texte) {
        this.texte = texte;
    }

    /**
     * Récupère la date de création du commentaire.
     *
     * @return La date de création du commentaire.
     */
    public Date getDateCommentaire() {
        return dateCommentaire;
    }

    /**
     * Définit la date de création du commentaire.
     *
     * @param dateCommentaire La date à définir.
     */
    public void setDateCommentaire(Date dateCommentaire) {
        this.dateCommentaire = dateCommentaire;
    }

    /**
     * Récupère l'utilisateur ayant créé le commentaire.
     *
     * @return L'utilisateur ayant créé le commentaire.
     */
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    /**
     * Définit l'utilisateur ayant créé le commentaire.
     *
     * @param utilisateur L'utilisateur à définir.
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    /**
     * Récupère le post associé au commentaire.
     *
     * @return Le post associé au commentaire.
     */
    public Post getPost() {
        return post;
    }

    /**
     * Définit le post associé au commentaire.
     *
     * @param post Le post à définir.
     */
    public void setPost(Post post) {
        this.post = post;
    }
}