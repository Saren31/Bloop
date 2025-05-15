package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.Optional;

/**
 * Service pour gérer les relations entre utilisateurs, comme l'envoi de demandes d'amitié.
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
//accepter une demande
    public String accepterDemandeAmitie(Long idReceveur, Long idEnvoyeur) {
        Optional<Utilisateur> optReceveur = utilisateurRepository.findById(idReceveur);
        Optional<Utilisateur> optEnvoyeur = utilisateurRepository.findById(idEnvoyeur);

        if (optReceveur.isEmpty() || optEnvoyeur.isEmpty()) {
            return "L'un des utilisateurs n'existe pas.";
        }

        Utilisateur receveur = optReceveur.get();
        Utilisateur envoyeur = optEnvoyeur.get();

        if (!receveur.getDemandesRecues().contains(envoyeur)) {
            return "Aucune demande à accepter de cet utilisateur.";
        }

        // Retirer la demande
        receveur.getDemandesRecues().remove(envoyeur);
        envoyeur.getDemandesEnvoyees().remove(receveur);


        // ajouter chacun dans la liste d’amis de l’autre
        receveur.getAmis().add(envoyeur);
        envoyeur.getAmis().add(receveur);

        // Sauvegarde des modifications
        utilisateurRepository.save(envoyeur);
        utilisateurRepository.save(receveur);

        return "Demande d'amitié acceptée.";
    }
}