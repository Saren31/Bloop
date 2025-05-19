package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.*;

@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean liked = Boolean.FALSE;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private Post post;

    // Getters et setters
    public Long getId() {
        return id;
    }

    public boolean isLiked() {
        return Boolean.TRUE.equals(this.liked);
    }

    public void setLiked(boolean like) {
        liked = like;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
