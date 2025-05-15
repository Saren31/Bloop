package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.Optional;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    public String envoyerDemandeAmitie(Long idEnvoyeur, Long idReceveur) {
        if (idEnvoyeur.equals(idReceveur)) {
            return "Vous ne pouvez pas vous envoyer une demande à vous-même.";
        }

        Optional<Utilisateur> optEnvoyeur = utilisateurRepository.findById(idEnvoyeur);
        Optional<Utilisateur> optReceveur = utilisateurRepository.findById(idReceveur);

        if (optEnvoyeur.isEmpty() || optReceveur.isEmpty()) {
            return "Utilisateur non trouvé.";
        }

        Utilisateur envoyeur = optEnvoyeur.get();
        Utilisateur receveur = optReceveur.get();

        if (envoyeur.getDemandesEnvoyees().contains(receveur)) {
            return "Demande déjà envoyée.";
        }

        envoyeur.getDemandesEnvoyees().add(receveur);
        utilisateurRepository.save(envoyeur);

        return "Demande d’amitié envoyée avec succès.";
    }


}
