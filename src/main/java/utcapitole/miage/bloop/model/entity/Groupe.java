package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente une entité JPA pour un groupe.
 * Cette classe est mappée à la table "groupe" dans la base de données.
 */
@Entity
@Table(name = "groupe")
public class Groupe implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


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

    /**
     * Liste des membres du groupe.
     * Relation Many-to-Many avec l'entité Utilisateur.
     */
    @ManyToMany(mappedBy = "groupes")
    private List<Utilisateur> membres = new ArrayList<>();

    /**
     * Retourne la liste des membres du groupe.
     *
     * @return Liste des membres.
     */
    public List<Utilisateur> getMembres() {
        return membres;
    }

    /**
     * Définit la liste des membres du groupe.
     *
     * @param membres Liste des membres.
     */
    public void setMembres(List<Utilisateur> membres) {
        this.membres = membres;
    }

    /**
     * Retourne l'identifiant du groupe.
     *
     * @return Identifiant du groupe.
     */
    public long getIdGroupe() {
        return idGroupe;
    }

    /**
     * Définit l'identifiant du groupe.
     *
     * @param idGroupe Identifiant du groupe.
     */
    public void setIdGroupe(long idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
     * Retourne le nom du groupe.
     *
     * @return Nom du groupe.
     */
    public String getNomGroupe() {
        return nomGroupe;
    }

    /**
     * Définit le nom du groupe.
     *
     * @param nomGroupe Nom du groupe.
     */
    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    /**
     * Retourne le logo du groupe.
     *
     * @return Logo du groupe sous forme de tableau de bytes.
     */
    public byte[] getLogoGroupe() {
        return logoGroupe;
    }

    /**
     * Définit le logo du groupe.
     *
     * @param logoGroupe Logo du groupe sous forme de tableau de bytes.
     */
    public void setLogoGroupe(byte[] logoGroupe) {
        this.logoGroupe = logoGroupe;
    }

    /**
     * Retourne le thème du groupe.
     *
     * @return Thème du groupe.
     */
    public String getThemeGroupe() {
        return themeGroupe;
    }

    /**
     * Définit le thème du groupe.
     *
     * @param themeGroupe Thème du groupe.
     */
    public void setThemeGroupe(String themeGroupe) {
        this.themeGroupe = themeGroupe;
    }

    /**
     * Retourne la description du groupe.
     *
     * @return Description du groupe.
     */
    public String getDescriptionGroupe() {
        return descriptionGroupe;
    }

    /**
     * Définit la description du groupe.
     *
     * @param descriptionGroupe Description du groupe.
     */
    public void setDescriptionGroupe(String descriptionGroupe) {
        this.descriptionGroupe = descriptionGroupe;
    }

    /**
     * Retourne le créateur du groupe.
     *
     * @return Créateur du groupe.
     */
    public Utilisateur getCreateurGroupe() {
        return createurGroupe;
    }

    /**
     * Définit le créateur du groupe.
     *
     * @param createurGroupe Créateur du groupe.
     */
    public void setCreateurGroupe(Utilisateur createurGroupe) {
        this.createurGroupe = createurGroupe;
    }

    /**
     * Retourne une représentation textuelle de l'objet Groupe.
     *
     * @return Représentation textuelle du groupe.
     */
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

    /**
     * Vérifie l'égalité entre deux objets Groupe.
     *
     * @param o Objet à comparer.
     * @return true si les objets sont égaux, false sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Groupe groupe = (Groupe) o;
        return getIdGroupe() == groupe.getIdGroupe() && Objects.equals(getNomGroupe(), groupe.getNomGroupe()) && Objects.equals(getLogoGroupe(), groupe.getLogoGroupe()) && Objects.equals(getThemeGroupe(), groupe.getThemeGroupe()) && Objects.equals(getDescriptionGroupe(), groupe.getDescriptionGroupe()) && Objects.equals(getCreateurGroupe(), groupe.getCreateurGroupe());
    }

    /**
     * Retourne le hash code de l'objet Groupe.
     *
     * @return Hash code du groupe.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getIdGroupe(), getNomGroupe(), getLogoGroupe(), getThemeGroupe(), getDescriptionGroupe(), getCreateurGroupe());
    }
}