package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Représente une entité JPA pour un groupe.
 * Cette classe est mappée à la table "groupe" dans la base de données.
 */
@Entity
@Table(name = "groupe")
public class Groupe {

    /**
     * Identifiant unique du groupe.
     * Généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idGroupe;

    /**
     * Nom du groupe.
     */
    private String nomGroupe;

    /**
     * Logo du groupe, stocké sous forme de tableau de bytes.
     */
    private byte[] logoGroupe;

    /**
     * Thème du groupe.
     */
    private String themeGroupe;

    /**
     * Description du groupe.
     */
    private String descriptionGroupe;

    /**
     * Créateur du groupe.
     * Relation Many-to-One avec l'entité Utilisateur.
     */
    @ManyToOne
    @JoinColumn(name = "id_createur")
    private Utilisateur createurGroupe;

    // Getters et setters

    public long getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(long idGroupe) {
        this.idGroupe = idGroupe;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public byte[] getLogoGroupe() {
        return logoGroupe;
    }

    public void setLogoGroupe(byte[] logoGroupe) {
        this.logoGroupe = logoGroupe;
    }

    public String getThemeGroupe() {
        return themeGroupe;
    }

    public void setThemeGroupe(String themeGroupe) {
        this.themeGroupe = themeGroupe;
    }

    public String getDescriptionGroupe() {
        return descriptionGroupe;
    }

    public void setDescriptionGroupe(String descriptionGroupe) {
        this.descriptionGroupe = descriptionGroupe;
    }

    public Utilisateur getCreateurGroupe() {
        return createurGroupe;
    }

    public void setCreateurGroupe(Utilisateur createurGroupe) {
        this.createurGroupe = createurGroupe;
    }


    @Override
    public String toString() {
        return "Groupe{" +
                "idGroupe=" + idGroupe +
                ", nomGroupe='" + nomGroupe + '\'' +
                ", logoGroupe=" + logoGroupe +
                ", themeGroupe='" + themeGroupe + '\'' +
                ", descriptionGroupe='" + descriptionGroupe + '\'' +
                ", createurGroupe=" + createurGroupe +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Groupe groupe = (Groupe) o;
        return getIdGroupe() == groupe.getIdGroupe() && Objects.equals(getNomGroupe(), groupe.getNomGroupe()) && Objects.equals(getLogoGroupe(), groupe.getLogoGroupe()) && Objects.equals(getThemeGroupe(), groupe.getThemeGroupe()) && Objects.equals(getDescriptionGroupe(), groupe.getDescriptionGroupe()) && Objects.equals(getCreateurGroupe(), groupe.getCreateurGroupe());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdGroupe(), getNomGroupe(), getLogoGroupe(), getThemeGroupe(), getDescriptionGroupe(), getCreateurGroupe());
    }
}