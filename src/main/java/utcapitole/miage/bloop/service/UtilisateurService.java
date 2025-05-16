package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.Optional;

@Service
public class UtilisateurService {

    // ------------------------ Pour US06 Voir mon profil ----------------------------//
    @Autowired
    public UtilisateurRepository utilisateurRepository;

    public Utilisateur getUtilisateurParId(long id) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        return utilisateur.orElse(null);
    }


}

