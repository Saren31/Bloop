package utcapitole.miage.bloop.dto;

/**
 * DTO (Data Transfer Object) représentant un résumé des informations d'un utilisateur.
 * Contient uniquement l'identifiant et le nom de l'utilisateur.
 */
public class UtilisateurSummaryDTO {
    private Long idUser; // L'identifiant unique de l'utilisateur
    private String nomUser; // Le nom de l'utilisateur

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
}