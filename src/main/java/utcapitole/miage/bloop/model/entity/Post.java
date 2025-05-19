package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

/**
 * Entité représentant un post dans le système.
 * Un post est associé à un utilisateur et peut contenir du texte, une image et une date de création.
 */
@Entity
@Table(name = "post")
public class Post {

    /**
     * Identifiant unique du post.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPost;

    /**
     * Texte du post.
     */
    private String textePost;

    /**
     * Image associée au post, stockée sous forme de tableau d'octets.
     */
    private byte[] imagePost;

    /**
     * Date et heure de création du post.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePost;

    @Transient
    private boolean likedByCurrentUser;

    @Transient
    private int likeCount;


    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    @Transient
    private boolean dislikedByCurrentUser;

    @Transient
    private int dislikeCount;


    public boolean isDislikedByCurrentUser() {
        return dislikedByCurrentUser;
    }

    public void setDislikedByCurrentUser(boolean dislikedByCurrentUser) {
        this.dislikedByCurrentUser = dislikedByCurrentUser;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }



    /**
     * Utilisateur ayant créé le post.
     */
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    /**
     * Récupère l'identifiant du post.
     *
     * @return L'identifiant du post.
     */

    public Long getIdPost() { return idPost; }

    /**
     * Définit l'identifiant du post.
     *
     * @param idPost L'identifiant à définir.
     */
    public void setIdPost(Long idPost) { this.idPost = idPost; }

    /**
     * Récupère le texte du post.
     *
     * @return Le texte du post.
     */
    public String getTextePost() { return textePost; }

    /**
     * Définit le texte du post.
     *
     * @param textePost Le texte à définir.
     */
    public void setTextePost(String textePost) { this.textePost = textePost; }

    /**
     * Récupère l'image associée au post.
     *
     * @return L'image du post sous forme de tableau d'octets.
     */
    public byte[] getImagePost() { return imagePost; }

    /**
     * Définit l'image associée au post.
     *
     * @param imagePost L'image à définir sous forme de tableau d'octets.
     */
    public void setImagePost(byte[] imagePost) { this.imagePost = imagePost; }

    /**
     * Récupère la date de création du post.
     *
     * @return La date de création du post.
     */
    public Date getDatePost() { return datePost; }

    /**
     * Définit la date de création du post.
     *
     * @param datePost La date à définir.
     */
    public void setDatePost(Date datePost) { this.datePost = datePost; }

    /**
     * Récupère l'utilisateur ayant créé le post.
     *
     * @return L'utilisateur ayant créé le post.
     */
    public Utilisateur getUtilisateur() { return utilisateur; }

    /**
     * Définit l'utilisateur ayant créé le post.
     *
     * @param utilisateur L'utilisateur à définir.
     */
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }




    /**
     * Vérifie l'égalité entre deux objets Post.
     *
     * @param o L'objet à comparer.
     * @return true si les objets sont égaux, sinon false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(idPost, post.idPost);
    }

    /**
     * Calcule le hash code de l'objet Post.
     *
     * @return Le hash code basé sur l'identifiant du post.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idPost);
    }
}