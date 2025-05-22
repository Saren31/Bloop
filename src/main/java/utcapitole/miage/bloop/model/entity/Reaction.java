package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.*;

/**
 * Entité représentant une réaction à un post.
 * Une réaction est associée à un post et à un utilisateur, et peut être de différents types.
 */
@Entity
public class Reaction {

    /**
     * Identifiant unique de la réaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Post auquel la réaction est associée.
     */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * Utilisateur ayant effectué la réaction.
     */
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    /**
     * Indique si la réaction est un "like".
     */
    private boolean liked;

    /**
     * Type de la réaction (par exemple, "like", "love", "angry", etc.).
     */
    private String type;

    /**
     * Récupère l'identifiant unique de la réaction.
     *
     * @return L'identifiant de la réaction.
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique de la réaction.
     *
     * @param id L'identifiant de la réaction.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupère le post associé à la réaction.
     *
     * @return Le post associé.
     */
    public Post getPost() {
        return post;
    }

    /**
     * Définit le post associé à la réaction.
     *
     * @param post Le post à associer.
     */
    public void setPost(Post post) {
        this.post = post;
    }

    /**
     * Récupère l'utilisateur ayant effectué la réaction.
     *
     * @return L'utilisateur associé.
     */
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    /**
     * Définit l'utilisateur ayant effectué la réaction.
     *
     * @param utilisateur L'utilisateur à associer.
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    /**
     * Indique si la réaction est un "like".
     *
     * @return `true` si c'est un "like", sinon `false`.
     */
    public boolean isLiked() {
        return liked;
    }

    /**
     * Définit si la réaction est un "like".
     *
     * @param liked `true` si c'est un "like", sinon `false`.
     */
    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    /**
     * Récupère le type de la réaction.
     *
     * @return Le type de la réaction.
     */
    public String getType() {
        return type;
    }

    /**
     * Définit le type de la réaction.
     *
     * @param type Le type de la réaction (par exemple, "like", "love", etc.).
     */
    public void setType(String type) {
        this.type = type;
    }
}