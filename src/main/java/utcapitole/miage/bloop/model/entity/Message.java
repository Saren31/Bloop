package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

/**
 * Entité représentant un message échangé entre deux utilisateurs.
 * Chaque message contient un expéditeur, un destinataire, un contenu,
 * et une date d'envoi.
 */
@Entity
public class Message {

    /**
     * Identifiant unique du message, généré automatiquement.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Utilisateur qui a envoyé le message.
     */
    @ManyToOne
    private Utilisateur expediteur;

    /**
     * Utilisateur qui reçoit le message.
     */
    @ManyToOne
    private Utilisateur destinataire;

    /**
     * Contenu textuel du message.
     */
    private String contenu;

    /**
     * Date et heure d'envoi du message.
     */
    private LocalDateTime dateEnvoi;

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
     * Obtient l'utilisateur qui a envoyé le message.
     *
     * @return L'expéditeur du message.
     */
    public Utilisateur getExpediteur() {
        return expediteur;
    }

    /**
     * Définit l'utilisateur qui a envoyé le message.
     *
     * @param expediteur L'expéditeur du message.
     */
    public void setExpediteur(Utilisateur expediteur) {
        this.expediteur = expediteur;
    }

    /**
     * Obtient l'utilisateur qui reçoit le message.
     *
     * @return Le destinataire du message.
     */
    public Utilisateur getDestinataire() {
        return destinataire;
    }

    /**
     * Définit l'utilisateur qui reçoit le message.
     *
     * @param destinataire Le destinataire du message.
     */
    public void setDestinataire(Utilisateur destinataire) {
        this.destinataire = destinataire;
    }

    /**
     * Obtient le contenu textuel du message.
     *
     * @return Le contenu du message.
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * Définit le contenu textuel du message.
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
}