package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Entity
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateDebut;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateFin;

    private String lieu;

    @ManyToOne
    @JoinColumn(name = "organisateur_id")
    private Utilisateur organisateur;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }

    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Date getDateDebut() { return dateDebut; }

    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }

    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public String getLieu() { return lieu; }

    public void setLieu(String lieu) { this.lieu = lieu; }

    public Utilisateur getOrganisateur() { return organisateur; }

    public void setOrganisateur(Utilisateur organisateur) { this.organisateur = organisateur; }
}
