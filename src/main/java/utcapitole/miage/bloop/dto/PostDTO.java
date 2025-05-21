package utcapitole.miage.bloop.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * DTO (Data Transfer Object) représentant un post.
 */
public class PostDTO {
    private String textePost;
    private MultipartFile imageFile;
    private Long idPost;
    private Date datePost;
    private Long utilisateurId;
    private Long groupeId;
    private UtilisateurSummaryDTO utilisateur; // Résumé des informations sur l'utilisateur

    public String getTextePost() {
        return textePost;
    }

    public void setTextePost(String textePost) {
        this.textePost = textePost;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public Long getIdPost() {
        return idPost;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

    public Date getDatePost() {
        return datePost;
    }

    public void setDatePost(Date datePost) {
        this.datePost = datePost;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Long getGroupeId() {
        return groupeId;
    }

    public void setGroupeId(Long groupeId) {
        this.groupeId = groupeId;
    }

    public UtilisateurSummaryDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurSummaryDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

}