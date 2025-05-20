package utcapitole.miage.bloop.dto;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) représentant un message échangé entre utilisateurs.
 * Contient les informations nécessaires pour afficher ou traiter un message.
 */
public class MessageDTO {
    private Long id; // Identifiant unique du message
    private String contenu; // Contenu du message
    private LocalDateTime dateEnvoi; // Date et heure d'envoi du message
    private UtilisateurSummary expediteur; // Résumé des informations sur l'expéditeur
    private Long destinataireId; // Identifiant du destinataire

    /**
     * Obtient l'identifiant unique du message.
     *
     * @return L'identifiant du message.
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique du message.
     *
     * @param id L'identifiant du message.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtient le contenu du message.
     *
     * @return Le contenu du message.
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * Définit le contenu du message.
     *
     * @param contenu Le contenu du message.
     */
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    /**
     * Obtient la date et l'heure d'envoi du message.
     *
     * @return La date et l'heure d'envoi.
     */
    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * Définit la date et l'heure d'envoi du message.
     *
     * @param dateEnvoi La date et l'heure d'envoi.
     */
    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    /**
     * Obtient les informations résumées sur l'expéditeur.
     *
     * @return Un objet contenant les informations de l'expéditeur.
     */
    public UtilisateurSummary getExpediteur() {
        return expediteur;
    }

    /**
     * Définit les informations résumées sur l'expéditeur.
     *
     * @param expediteur Un objet contenant les informations de l'expéditeur.
     */
    public void setExpediteur(UtilisateurSummary expediteur) {
        this.expediteur = expediteur;
    }


    /**
     * Obtient l'identifiant du destinataire.
     *
     * @return L'identifiant du destinataire.
     */
    public Long getDestinataireId() {
        return destinataireId;
    }

    /**
     * Définit l'identifiant du destinataire.
     *
     * @param destinataireId L'identifiant du destinataire.
     */
    public void setDestinataireId(Long destinataireId) {
        this.destinataireId = destinataireId;
    }

    /**
     * Classe interne représentant un résumé des informations d'un utilisateur.
     */
    public static class UtilisateurSummary {
        private Long idUser; // Identifiant unique de l'utilisateur
        private String nomUser; // Nom de l'utilisateur

        /**
         * Obtient l'identifiant unique de l'utilisateur.
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
         * Obtient le nom de l'utilisateur.
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
}