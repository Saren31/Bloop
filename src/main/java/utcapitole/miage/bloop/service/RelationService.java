package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.dto.UtilisateurDTO;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour gérer les relations entre utilisateurs, comme l'envoi de demandes d'amitié,
 * la gestion des demandes, la consultation de la liste d'amis et la suppression d'amis.
 */
@Service
public class RelationService {

    private UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur pour injecter le dépôt des utilisateurs.
     *
     * @param utilisateurRepository Le dépôt pour interagir avec les utilisateurs.
     */
    @Autowired
    public RelationService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Envoie une demande d'amitié d'un utilisateur à un autre.
     *
     * @param idEnvoyeur L'identifiant de l'utilisateur envoyant la demande.
     * @param idReceveur L'identifiant de l'utilisateur recevant la demande.
     * @return Un message indiquant le résultat de l'opération.
     */
    public String envoyerDemandeAmitie(Long idEnvoyeur, Long idReceveur) {
        if (idEnvoyeur.equals(idReceveur)) {
            return "Vous ne pouvez pas vous envoyer une demande à vous-même.";
        }

        Optional<Utilisateur> optEnvoyeur = utilisateurRepository.findById(idEnvoyeur);
        Optional<Utilisateur> optReceveur = utilisateurRepository.findById(idReceveur);

        if (optEnvoyeur.isEmpty() || optReceveur.isEmpty()) {
            return "L'un des utilisateurs n'existe pas.";
        }

        Utilisateur envoyeur = optEnvoyeur.get();
        Utilisateur receveur = optReceveur.get();

        // Vérifie si la demande a déjà été envoyée
        if (envoyeur.getDemandesEnvoyees().contains(receveur)) {
            return "Vous avez déjà envoyé une demande à cet utilisateur.";
        }

        // Ajoute la demande côté envoyeur
        envoyeur.getDemandesEnvoyees().add(receveur);

        // Ajoute la demande côté receveur
        receveur.getDemandesRecues().add(envoyeur);

        // Sauvegarde des deux utilisateurs
        utilisateurRepository.save(envoyeur);
        utilisateurRepository.save(receveur);

        return "Demande d’amitié envoyée avec succès.";
    }

    /**
     * Gère une demande d'amitié (acceptation ou refus).
     *
     * @param idReceveur L'identifiant de l'utilisateur recevant la demande.
     * @param idEnvoyeur L'identifiant de l'utilisateur ayant envoyé la demande.
     * @param accepter   Indique si la demande est acceptée (true) ou refusée (false).
     * @return Un message indiquant le résultat de l'opération.
     */
    public String gererDemandeAmitie(Long idReceveur, Long idEnvoyeur, boolean accepter) {
        Optional<Utilisateur> optReceveur = utilisateurRepository.findById(idReceveur);
        Optional<Utilisateur> optEnvoyeur = utilisateurRepository.findById(idEnvoyeur);

        if (optReceveur.isEmpty() || optEnvoyeur.isEmpty()) {
            return "L'un des utilisateurs n'existe pas.";
        }

        Utilisateur receveur = optReceveur.get();
        Utilisateur envoyeur = optEnvoyeur.get();

        if (!receveur.getDemandesRecues().contains(envoyeur)) {
            return "Aucune demande à traiter de cet utilisateur.";
        }

        // Si la demande est acceptée
        if (accepter) {
            receveur.getDemandesRecues().remove(envoyeur);
            envoyeur.getDemandesEnvoyees().remove(receveur);

            receveur.getAmis().add(envoyeur);
            envoyeur.getAmis().add(receveur);

            utilisateurRepository.save(envoyeur);
            utilisateurRepository.save(receveur);

            return "Demande d'amitié acceptée.";
        } else {
            // Si la demande est refusée
            receveur.getDemandesRecues().remove(envoyeur);
            envoyeur.getDemandesEnvoyees().remove(receveur);

            utilisateurRepository.save(envoyeur);
            utilisateurRepository.save(receveur);

            return "Demande d'amitié refusée.";
        }
    }

    /**
     * Récupère la liste des amis d'un utilisateur sous forme de DTOs.
     *
     * @param idUser L'identifiant de l'utilisateur.
     * @return Une liste contenant les DTOs des amis de l'utilisateur, ou une liste vide si l'utilisateur n'existe pas.
     */
    public List<UtilisateurDTO> getListeAmis(Long idUser) {
        Optional<Utilisateur> optUser = utilisateurRepository.findById(idUser);

        return optUser.map(utilisateur -> utilisateur.getAmis().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList())).orElseGet(ArrayList::new);

    }

    /**
     * Convertit une entité Utilisateur en DTO.
     */
    private UtilisateurDTO convertToDTO(Utilisateur utilisateur) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setIdUser(utilisateur.getIdUser());
        dto.setNomUser(utilisateur.getNomUser());
        dto.setPrenomUser(utilisateur.getPrenomUser());
        dto.setPseudoUser(utilisateur.getPseudoUser());
        dto.setEmailUser(utilisateur.getEmailUser());
        return dto;
    }

    /**
     * Supprime un ami de la liste d'amis d'un utilisateur.
     *
     * @param idUser L'identifiant de l'utilisateur effectuant la suppression.
     * @param idAmi  L'identifiant de l'ami à supprimer.
     * @return Un message indiquant le résultat de l'opération.
     */
    public String supprimerAmi(Long idUser, Long idAmi) {
        Optional<Utilisateur> optUser = utilisateurRepository.findById(idUser);
        Optional<Utilisateur> optAmi = utilisateurRepository.findById(idAmi);

        if (optUser.isEmpty() || optAmi.isEmpty()) {
            return "Utilisateur non trouvé.";
        }

        Utilisateur user = optUser.get();
        Utilisateur ami = optAmi.get();

        if (!user.getAmis().contains(ami)) {
            return "Cet utilisateur n’est pas dans votre liste d’amis.";
        }

        user.getAmis().remove(ami);
        ami.getAmis().remove(user);

        utilisateurRepository.save(user);
        utilisateurRepository.save(ami);

        return "Ami supprimé avec succès.";
    }
}