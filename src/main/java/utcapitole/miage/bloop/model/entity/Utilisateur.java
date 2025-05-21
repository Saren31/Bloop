package utcapitole.miage.bloop.model.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Classe Utilisateur
 * Représente un utilisateur de l'application.
 */
@Entity
@Table(name = "utilisateur")
public class Utilisateur implements UserDetails {

    /**
     * Identifiant unique de l'utilisateur.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idUser;

    /**
     * Nom de l'utilisateur.
     */
    private String nomUser;

    /**
     * Prénom de l'utilisateur.
     */
    private String prenomUser;

    /**
     * Adresse e-mail de l'utilisateur.
     */
    private String emailUser;

    /**
     * Mot de passe de l'utilisateur.
     */
    private String mdpUser;

    /**
     * Pseudonyme de l'utilisateur.
     */
    private String pseudoUser;

    /**
     * Avatar de l'utilisateur sous forme de tableau d'octets.
     */
    private byte[] avatarUser;

    /**
     * Numéro de téléphone de l'utilisateur.
     */
    private String telUser;

    /**
     * Indique si l'utilisateur est visible.
     */
    private boolean visibiliteUser;

    /**
     * Indique si l'inscription de l'utilisateur est validée.
     */
    private boolean validerInscription;

    /**
     * Token utilisé pour la validation de l'inscription.
     */
    private String tokenInscription;

    /**
     * Liste des demandes d'amitié envoyées par l'utilisateur.
     */
    @ManyToMany
    @JoinTable(
            name = "demandes_envoyees",
            joinColumns = @JoinColumn(name = "envoyeur_id"),
            inverseJoinColumns = @JoinColumn(name = "receveur_id")
    )
    private List<Utilisateur> demandesEnvoyees = new ArrayList<>();

    /**
     * Liste des demandes d'amitié reçues par l'utilisateur.
     * Mappée depuis l'autre côté de la relation.
     */
    @ManyToMany(mappedBy = "demandesEnvoyees")
    private List<Utilisateur> demandesRecues = new ArrayList<>();

    /**
     * Gerer la Liste des amis
     */
    @ManyToMany
    @JoinTable(
            name = "amis",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "ami_id")
    )
    private List<Utilisateur> amis = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "participer",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_groupe")
    )
    private List<Groupe> groupes = new ArrayList<>();





    public List<Groupe> getGroupes() {
        return groupes;
    }

    public void setGroupes(List<Groupe> groupes) {
        this.groupes = groupes;
    }

    /**
     * Récupère la liste des demandes d'amitié reçues.
     *
     * @return La liste des utilisateurs ayant envoyé une demande.
     */
    public List<Utilisateur> getDemandesRecues() {
        return demandesRecues;
    }

    /**
     * Définit la liste des demandes d'amitié reçues.
     *
     * @param demandesRecues La liste des utilisateurs à définir.
     */
    public void setDemandesRecues(List<Utilisateur> demandesRecues) {
        this.demandesRecues = demandesRecues;
    }

    /**
     * Récupère les autorités de l'utilisateur.
     *
     * @return Une collection vide (à implémenter si nécessaire).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Récupère le mot de passe de l'utilisateur.
     *
     * @return Le mot de passe.
     */
    @Override
    public String getPassword() {
        return mdpUser;
    }

    /**
     * Récupère le nom d'utilisateur (adresse e-mail).
     *
     * @return L'adresse e-mail.
     */
    @Override
    public String getUsername() {
        return emailUser;
    }

    /**
     * Indique si le compte de l'utilisateur n'est pas expiré.
     *
     * @return Toujours true (à personnaliser si nécessaire).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indique si le compte de l'utilisateur n'est pas verrouillé.
     *
     * @return Toujours true (à personnaliser si nécessaire).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indique si les informations d'identification de l'utilisateur ne sont pas expirées.
     *
     * @return Toujours true (à personnaliser si nécessaire).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indique si l'utilisateur est activé.
     *
     * @return Toujours true (à personnaliser si nécessaire).
     */
    @Override
    public boolean isEnabled() {
        return validerInscription;
    }

    /**
     * Récupère l'identifiant unique de l'utilisateur.
     *
     * @return L'identifiant.
     */
    public long getIdUser() {
        return idUser;
    }

    /**
     * Définit l'identifiant unique de l'utilisateur.
     *
     * @param idUser L'identifiant à définir.
     */
    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    /**
     * Récupère le nom de l'utilisateur.
     *
     * @return Le nom.
     */
    public String getNomUser() {
        return nomUser;
    }

    /**
     * Définit le nom de l'utilisateur.
     *
     * @param nomUser Le nom à définir.
     */
    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    /**
     * Récupère le prénom de l'utilisateur.
     *
     * @return Le prénom.
     */
    public String getPrenomUser() {
        return prenomUser;
    }

    /**
     * Définit le prénom de l'utilisateur.
     *
     * @param prenomUser Le prénom à définir.
     */
    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    /**
     * Définit l'adresse e-mail de l'utilisateur.
     *
     * @param emailUser L'adresse e-mail à définir.
     */
    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     *
     * @param mdpUser Le mot de passe à définir.
     */
    public void setMdpUser(String mdpUser) {
        this.mdpUser = mdpUser;
    }

    /**
     * Récupère le pseudonyme de l'utilisateur.
     *
     * @return Le pseudonyme.
     */
    public String getPseudoUser() {
        return pseudoUser;
    }

    /**
     * Définit le pseudonyme de l'utilisateur.
     *
     * @param pseudoUser Le pseudonyme à définir.
     */
    public void setPseudoUser(String pseudoUser) {
        this.pseudoUser = pseudoUser;
    }

    /**
     * Récupère l'avatar de l'utilisateur.
     *
     * @return L'avatar sous forme de tableau d'octets.
     */
    public byte[] getAvatarUser() {
        return avatarUser;
    }

    /**
     * Définit l'avatar de l'utilisateur.
     *
     * @param avatarUser L'avatar à définir.
     */
    public void setAvatarUser(byte[] avatarUser) {
        this.avatarUser = avatarUser;
    }

    /**
     * Récupère le numéro de téléphone de l'utilisateur.
     *
     * @return Le numéro de téléphone.
     */
    public String getTelUser() {
        return telUser;
    }

    /**
     * Définit le numéro de téléphone de l'utilisateur.
     *
     * @param telUser Le numéro de téléphone à définir.
     */
    public void setTelUser(String telUser) {
        this.telUser = telUser;
    }

    /**
     * Indique si l'utilisateur est visible.
     *
     * @return true si l'utilisateur est visible, false sinon.
     */
    public boolean isVisibiliteUser() {
        return visibiliteUser;
    }

    /**
     * Définit la visibilité de l'utilisateur.
     *
     * @param visibiliteUser true pour rendre l'utilisateur visible, false sinon.
     */
    public void setVisibiliteUser(boolean visibiliteUser) {
        this.visibiliteUser = visibiliteUser;
    }

    /**
     * Définit si l'inscription de l'utilisateur est validée.
     *
     * @param validerInscription true pour valider l'inscription, false sinon.
     */
    public void setValiderInscription(boolean validerInscription) {
        this.validerInscription = validerInscription;
    }

    /**
     * Récupère le token d'inscription de l'utilisateur.
     *
     * @return Le token d'inscription.
     */
    public String getTokenInscription() {
        return tokenInscription;
    }

    /**
     * Définit le token d'inscription de l'utilisateur.
     *
     * @param tokenInscription Le token à définir.
     */
    public void setTokenInscription(String tokenInscription) {
        this.tokenInscription = tokenInscription;
    }




    /**
     * Récupère la liste des demandes d'amitié envoyées.
     *
     * @return La liste des utilisateurs ayant reçu une demande.
     */
    public List<Utilisateur> getDemandesEnvoyees() {
        return demandesEnvoyees;
    }

    /**
     * Définit la liste des demandes d'amitié envoyées.
     *
     * @param demandesEnvoyees La liste des utilisateurs à définir.
     */
    public void setDemandesEnvoyees(List<Utilisateur> demandesEnvoyees) {
        this.demandesEnvoyees = demandesEnvoyees;
    }




    public List<Utilisateur> getAmis() {
        return amis;
    }

    public void setAmis(List<Utilisateur> amis) {
        this.amis = amis;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return getIdUser() == that.getIdUser() && isVisibiliteUser() == that.isVisibiliteUser() && Objects.equals(getNomUser(), that.getNomUser()) && Objects.equals(getPrenomUser(), that.getPrenomUser()) && Objects.equals(emailUser, that.emailUser) && Objects.equals(mdpUser, that.mdpUser) && Objects.equals(getPseudoUser(), that.getPseudoUser()) && Objects.deepEquals(getAvatarUser(), that.getAvatarUser()) && Objects.equals(getTelUser(), that.getTelUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdUser(), getNomUser(), getPrenomUser(), emailUser, mdpUser, getPseudoUser(), Arrays.hashCode(getAvatarUser()), getTelUser(), isVisibiliteUser());
    }

    /**
     * Génère une représentation sous forme de chaîne de l'objet Utilisateur.
     *
     * @return La chaîne représentant l'objet.
     */
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