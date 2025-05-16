package utcapitole.miage.bloop.model.entity;


import jakarta.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * Classe Post
 * Représente une publication textuelle ou avec image
 */
@Entity
@Table(name = "post")
public class Post  {

    /**
     * Identifiant unique du post
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPost;

    private String textePost;

    @Lob
    private byte[] imagePost;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datePost;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    public Post() {
        this.datePost = new Date();
    }

    public long getIdPost() {
        return idPost;
    }

    public void setIdPost(long idPost) {
        this.idPost = idPost;
    }

    public String getTextePost() {
        return textePost;
    }

    public void setTextePost(String textePost) {
        this.textePost = textePost;
    }

    public byte[] getImagePost() {
        return imagePost;
    }

    public void setImagePost(byte[] imagePost) {
        this.imagePost = imagePost;
    }

    public Date getDatePost() {
        return datePost;
    }

    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return idPost == post.idPost &&
                Objects.equals(textePost, post.textePost) &&
                Arrays.equals(imagePost, post.imagePost) &&
                Objects.equals(datePost, post.datePost) &&
                Objects.equals(utilisateur, post.utilisateur);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idPost, textePost, datePost, utilisateur);
        result = 31 * result + Arrays.hashCode(imagePost);
        return result;
    }

    @Override
    public String toString() {
        return "Post{" +
                "idPost=" + idPost +
                ", textePost='" + textePost + '\'' +
                ", imagePost=" + Arrays.toString(imagePost) +
                ", datePost=" + datePost +
                ", utilisateur=" + (utilisateur != null ? utilisateur.getIdUser() : "null") +
                '}';
    }
}

