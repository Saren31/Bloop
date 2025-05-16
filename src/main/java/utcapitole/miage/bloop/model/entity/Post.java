package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPost;

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


    public Long getIdPost() { return idPost; }
    public void setIdPost(Long idPost) { this.idPost = idPost; }
    public String getTextePost() { return textePost; }
    public void setTextePost(String textePost) { this.textePost = textePost; }
    public byte[] getImagePost() { return imagePost; }
    public void setImagePost(byte[] imagePost) { this.imagePost = imagePost; }
    public Date getDatePost() { return datePost; }
    public void setDatePost(Date datePost) { this.datePost = datePost; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(idPost, post.idPost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPost);
    }
}
