package utcapitole.miage.bloop.dto;

/**
 * DTO représentant un résumé des informations d'un utilisateur.
 */
public class UtilisateurSummaryDTO {
    private Long idUser;
    private String nomUser;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }
}