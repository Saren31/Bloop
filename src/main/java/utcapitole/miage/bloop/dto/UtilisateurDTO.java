package utcapitole.miage.bloop.dto;

/**
 * DTO (Data Transfer Object) représentant un utilisateur.
 * Contient les informations nécessaires pour transférer les données d'un utilisateur
 * entre les différentes couches de l'application.
 */
public class UtilisateurDTO {
    private Long idUser; // L'identifiant unique de l'utilisateur
    private String nomUser; // Le nom de l'utilisateur
    private String prenomUser; // Le prénom de l'utilisateur
    private String pseudoUser; // Le pseudonyme de l'utilisateur
    private String emailUser; // L'adresse e-mail de l'utilisateur

    /**
     * Récupère l'identifiant unique de l'utilisateur.
     *
     * @return L'identifiant de l'utilisateur.
     */
    public Long getIdUser() {
        return idUser;
    }

    /**
     * Définit l'identifiant unique de l'utilisateur.
     *
     * @param idUser L'identifiant de l'utilisateur.
     */
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    /**
     * Récupère le nom de l'utilisateur.
     *
     * @return Le nom de l'utilisateur.
     */
    public String getNomUser() {
        return nomUser;
    }

    /**
     * Définit le nom de l'utilisateur.
     *
     * @param nomUser Le nom de l'utilisateur.
     */
    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    /**
     * Récupère le prénom de l'utilisateur.
     *
     * @return Le prénom de l'utilisateur.
     */
    public String getPrenomUser() {
        return prenomUser;
    }

    /**
     * Définit le prénom de l'utilisateur.
     *
     * @param prenomUser Le prénom de l'utilisateur.
     */
    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    /**
     * Récupère le pseudonyme de l'utilisateur.
     *
     * @return Le pseudonyme de l'utilisateur.
     */
    public String getPseudoUser() {
        return pseudoUser;
    }

    /**
     * Définit le pseudonyme de l'utilisateur.
     *
     * @param pseudoUser Le pseudonyme de l'utilisateur.
     */
    public void setPseudoUser(String pseudoUser) {
        this.pseudoUser = pseudoUser;
    }

    /**
     * Récupère l'adresse e-mail de l'utilisateur.
     *
     * @return L'adresse e-mail de l'utilisateur.
     */
    public String getEmailUser() {
        return emailUser;
    }

    /**
     * Définit l'adresse e-mail de l'utilisateur.
     *
     * @param emailUser L'adresse e-mail de l'utilisateur.
     */
    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }
}