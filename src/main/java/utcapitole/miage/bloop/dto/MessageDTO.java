package utcapitole.miage.bloop.dto;

import utcapitole.miage.bloop.model.entity.Utilisateur;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private String contenu;
    private LocalDateTime dateEnvoi;
    private UtilisateurSummary expediteur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public UtilisateurSummary getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(UtilisateurSummary expediteur) {
        this.expediteur = expediteur;
    }

    private Long expediteurId;
    private Long destinataireId;

    public Long getExpediteurId() { return expediteurId; }
    public void setExpediteurId(Long expediteurId) { this.expediteurId = expediteurId; }

    public Long getDestinataireId() { return destinataireId; }
    public void setDestinataireId(Long destinataireId) { this.destinataireId = destinataireId; }

    public static class UtilisateurSummary {
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
}

