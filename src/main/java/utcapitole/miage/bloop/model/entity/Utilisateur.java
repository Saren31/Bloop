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
     * Liste des amis de l'utilisateur.
     */
    @ManyToMany
    @JoinTable(
            name = "amis",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "ami_id")
    )
    private List<Utilisateur> amis = new ArrayList<>();

    /**
     * Liste des groupes auxquels l'utilisateur participe.
     */
    @ManyToMany
    @JoinTable(
            name = "participer",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_groupe")
    )
    private List<Groupe> groupes = new ArrayList<>();

    /**
     * Récupère la liste des groupes auxquels l'utilisateur participe.
     *
     * @return Liste des groupes.
     */
    public List<Groupe> getGroupes() {
        return groupes;
    }

    /**
     * Définit la liste des groupes auxquels l'utilisateur participe.
     *
     * @param groupes Liste des groupes.
     */
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
        return true;
    }
}