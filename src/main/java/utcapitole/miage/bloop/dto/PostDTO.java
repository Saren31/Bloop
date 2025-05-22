package utcapitole.miage.bloop.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * DTO (Data Transfer Object) représentant un post.
 * Contient les informations nécessaires pour transférer les données d'un post
 * entre les différentes couches de l'application.
 */
public class PostDTO {
    private String textePost; // Le texte du post
    private MultipartFile imageFile; // Le fichier image associé au post
    private Long idPost; // L'identifiant unique du post
    private Date datePost; // La date de création ou de publication du post
    private Long utilisateurId; // L'identifiant de l'utilisateur ayant créé le post
    private Long groupeId; // L'identifiant du groupe auquel le post appartient
    private UtilisateurSummaryDTO utilisateur; // Résumé des informations sur l'utilisateur

    /**
     * Récupère le texte du post.
     *
     * @return Le texte du post.
     */
    public String getTextePost() {
        return textePost;
    }

    /**
     * Définit le texte du post.
     *
     * @param textePost Le texte du post.
     */
    public void setTextePost(String textePost) {
        this.textePost = textePost;
    }

    /**
     * Récupère le fichier image associé au post.
     *
     * @return Le fichier image du post.
     */
    public MultipartFile getImageFile() {
        return imageFile;
    }

    /**
     * Définit le fichier image associé au post.
     *
     * @param imageFile Le fichier image du post.
     */
    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * Récupère l'identifiant unique du post.
     *
     * @return L'identifiant du post.
     */
    public Long getIdPost() {
        return idPost;
    }

    /**
     * Définit l'identifiant unique du post.
     *
     * @param idPost L'identifiant du post.
     */
    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

    /**
     * Récupère la date de création ou de publication du post.
     *
     * @return La date du post.
     */
    public Date getDatePost() {
        return datePost;
    }

    /**
     * Définit la date de création ou de publication du post.
     *
     * @param datePost La date du post.
     */
    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }

    /**
     * Récupère l'identifiant de l'utilisateur ayant créé le post.
     *
     * @return L'identifiant de l'utilisateur.
     */
    public Long getUtilisateurId() {
        return utilisateurId;
    }

    /**
     * Définit l'identifiant de l'utilisateur ayant créé le post.
     *
     * @param utilisateurId L'identifiant de l'utilisateur.
     */
    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    /**
     * Récupère l'identifiant du groupe auquel le post appartient.
     *
     * @return L'identifiant du groupe.
     */
    public Long getGroupeId() {
        return groupeId;
    }

    /**
     * Définit l'identifiant du groupe auquel le post appartient.
     *
     * @param groupeId L'identifiant du groupe.
     */
    public void setGroupeId(Long groupeId) {
        this.groupeId = groupeId;
    }

    /**
     * Récupère le résumé des informations sur l'utilisateur.
     *
     * @return Les informations résumées de l'utilisateur.
     */
    public UtilisateurSummaryDTO getUtilisateur() {
        return utilisateur;
    }

    /**
     * Définit le résumé des informations sur l'utilisateur.
     *
     * @param utilisateur Les informations résumées de l'utilisateur.
     */
    public void setUtilisateur(UtilisateurSummaryDTO utilisateur) {
        this.utilisateur = utilisateur;
    }
}