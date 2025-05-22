package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

/**
 * Entité représentant un événement.
 * Contient les informations nécessaires pour décrire un événement,
 * telles que le titre, la description, les dates, le lieu, l'organisateur,
 * ainsi que les utilisateurs inscrits, intéressés et participants.
 */
@Entity
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // L'identifiant unique de l'événement

    private String titre; // Le titre de l'événement
    private String description; // La description de l'événement

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateDebut; // La date et l'heure de début de l'événement

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateFin; // La date et l'heure de fin de l'événement

    private String lieu; // Le lieu où se déroule l'événement

    @ManyToOne
    @JoinColumn(name = "organisateur_id")
    private Utilisateur organisateur; // L'utilisateur organisateur de l'événement

    @ManyToMany
    @JoinTable(
            name = "evenement_inscriptions",
            joinColumns = @JoinColumn(name = "evenement_id"),
            inverseJoinColumns = @JoinColumn(name = "utilisateur_id")
    )
    private Set<Utilisateur> inscrits; // Les utilisateurs inscrits à l'événement

    @ManyToMany
    @JoinTable(
            name = "evenement_interesses",
            joinColumns = @JoinColumn(name = "evenement_id"),
            inverseJoinColumns = @JoinColumn(name = "utilisateur_id")
    )
    private Set<Utilisateur> interesses; // Les utilisateurs intéressés par l'événement

    /**
     * Récupère les utilisateurs inscrits à l'événement.
     *
     * @return Les utilisateurs inscrits.
     */
    public Set<Utilisateur> getInscrits() {
        return inscrits;
    }

    /**
     * Définit les utilisateurs inscrits à l'événement.
     *
     * @param inscrits Les utilisateurs inscrits.
     */
    public void setInscrits(Set<Utilisateur> inscrits) {
        this.inscrits = inscrits;
    }

    /**
     * Récupère les utilisateurs intéressés par l'événement.
     *
     * @return Les utilisateurs intéressés.
     */
    public Set<Utilisateur> getInteresses() {
        return interesses;
    }

    /**
     * Définit les utilisateurs intéressés par l'événement.
     *
     * @param interesses Les utilisateurs intéressés.
     */
    public void setInteresses(Set<Utilisateur> interesses) {
        this.interesses = interesses;
    }

    /**
     * Récupère l'identifiant unique de l'événement.
     *
     * @return L'identifiant de l'événement.
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique de l'événement.
     *
     * @param id L'identifiant de l'événement.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupère le titre de l'événement.
     *
     * @return Le titre de l'événement.
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Définit le titre de l'événement.
     *
     * @param titre Le titre de l'événement.
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Récupère la description de l'événement.
     *
     * @return La description de l'événement.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la description de l'événement.
     *
     * @param description La description de l'événement.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Récupère la date et l'heure de début de l'événement.
     *
     * @return La date et l'heure de début.
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * Définit la date et l'heure de début de l'événement.
     *
     * @param dateDebut La date et l'heure de début.
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Récupère la date et l'heure de fin de l'événement.
     *
     * @return La date et l'heure de fin.
     */
    public Date getDateFin() {
        return dateFin;
    }

    /**
     * Définit la date et l'heure de fin de l'événement.
     *
     * @param dateFin La date et l'heure de fin.
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Récupère le lieu de l'événement.
     *
     * @return Le lieu de l'événement.
     */
    public String getLieu() {
        return lieu;
    }

    /**
     * Définit le lieu de l'événement.
     *
     * @param lieu Le lieu de l'événement.
     */
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    /**
     * Récupère l'utilisateur organisateur de l'événement.
     *
     * @return L'organisateur de l'événement.
     */
    public Utilisateur getOrganisateur() {
        return organisateur;
    }

    /**
     * Définit l'utilisateur organisateur de l'événement.
     *
     * @param organisateur L'organisateur de l'événement.
     */
    public void setOrganisateur(Utilisateur organisateur) {
        this.organisateur = organisateur;
    }



}