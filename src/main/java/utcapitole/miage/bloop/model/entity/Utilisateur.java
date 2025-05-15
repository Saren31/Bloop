package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.*;

import java.util.*;

/**
 * Classe Utilisateur
 * Représente un utilisateur de l'application
 */
@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    /**
     * Identifiant unique de l'utilisateur
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;

    private String nomUser;
    private String prenomUser;
    private String emailUser;
    private String mdpUser;
    private String pseudoUser;
    private byte[] avatarUser;
    private String telUser;
    private boolean visibiliteUser;
    private boolean validerInscription;
    private String tokenInscription;

    public Utilisateur() {
    }
    //demande envoyé
    @ManyToMany
    @JoinTable(
            name = "demandes_envoyees",
            joinColumns = @JoinColumn(name = "envoyeur_id"),
            inverseJoinColumns = @JoinColumn(name = "receveur_id")
    )

    private List<Utilisateur> demandesEnvoyees = new ArrayList<>();
    public List<Utilisateur> getDemandesEnvoyees() {
        return demandesEnvoyees;
    }
    public void setDemandesEnvoyees(List<Utilisateur> demandesEnvoyees) {
        this.demandesEnvoyees = demandesEnvoyees;
    }

    //demande recues
    // Demandes reçues (mappées depuis l'autre côté)
    @ManyToMany(mappedBy = "demandesEnvoyees")
    private List<Utilisateur> demandesRecues = new ArrayList<>();
    public List<Utilisateur> getDemandesRecues() {
        return demandesRecues;
    }
    public void setDemandesRecues(List<Utilisateur> demandesRecues) {
        this.demandesRecues = demandesRecues;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getMdpUser() {
        return mdpUser;
    }

    public void setMdpUser(String mdpUser) {
        this.mdpUser = mdpUser;
    }

    public String getPseudoUser() {
        return pseudoUser;
    }

    public void setPseudoUser(String pseudoUser) {
        this.pseudoUser = pseudoUser;
    }

    public byte[] getAvatarUser() {
        return avatarUser;
    }

    public void setAvatarUser(byte[] avatarUser) {
        this.avatarUser = avatarUser;
    }

    public String getTelUser() {
        return telUser;
    }

    public void setTelUser(String telUser) {
        this.telUser = telUser;
    }

    public boolean isVisibiliteUser() {
        return visibiliteUser;
    }

    public void setVisibiliteUser(boolean visibiliteUser) {
        this.visibiliteUser = visibiliteUser;
    }

    public boolean isValiderInscription() {
        return validerInscription;
    }

    public void setValiderInscription(boolean validerInscription) {
        this.validerInscription = validerInscription;
    }

    public String getTokenInscription() {
        return tokenInscription;
    }

    public void setTokenInscription(String tokenInscription) {
        this.tokenInscription = tokenInscription;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return getIdUser() == that.getIdUser() && isVisibiliteUser() == that.isVisibiliteUser() && Objects.equals(getNomUser(), that.getNomUser()) && Objects.equals(getPrenomUser(), that.getPrenomUser()) && Objects.equals(getEmailUser(), that.getEmailUser()) && Objects.equals(getMdpUser(), that.getMdpUser()) && Objects.equals(getPseudoUser(), that.getPseudoUser()) && Objects.deepEquals(getAvatarUser(), that.getAvatarUser()) && Objects.equals(getTelUser(), that.getTelUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdUser(), getNomUser(), getPrenomUser(), getEmailUser(), getMdpUser(), getPseudoUser(), Arrays.hashCode(getAvatarUser()), getTelUser(), isVisibiliteUser());
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUser=" + idUser +
                ", nomUser='" + nomUser + '\'' +
                ", prenomUser='" + prenomUser + '\'' +
                ", emailUser='" + emailUser + '\'' +
                ", mdpUser='" + mdpUser + '\'' +
                ", pseudoUser='" + pseudoUser + '\'' +
                ", avatarUser=" + Arrays.toString(avatarUser) +
                ", telUser='" + telUser + '\'' +
                ", visibiliteUser=" + visibiliteUser +
                '}';
    }
}
