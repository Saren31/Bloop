package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.EvenementRepository;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Service
public class EvenementService {

    private final EvenementRepository evenementRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    public EvenementService(EvenementRepository evenementRepository) {
        this.evenementRepository = evenementRepository;
    }

    public void creerEvenement(Evenement evenement) {
        evenementRepository.save(evenement);
    }

    public List<Evenement> getEvenementsParOrganisateur(Long idUser) {
        return evenementRepository.findByOrganisateur_IdUser(idUser);
    }

    public Evenement getEvenementParId(Long id) {
        return evenementRepository.findById(id).orElse(null);
    }


    public void inscrireUtilisateur(Evenement evenement, Utilisateur utilisateur) {
        if (evenement.getInscrits() == null) {
            evenement.setInscrits(new HashSet<>());
        }

        Utilisateur userFromDb = utilisateurRepository.findById(utilisateur.getIdUser())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + utilisateur.getIdUser()));

        if (!evenement.getInscrits().contains(userFromDb)) {
            evenement.getInscrits().add(userFromDb);
            evenementRepository.save(evenement);
        }
    }


    public void retirerUtilisateur(Evenement evenement, Utilisateur utilisateur) {
        if (evenement.getInscrits().contains(utilisateur)) {
            evenement.getInscrits().remove(utilisateur);
            evenementRepository.save(evenement);
        }
    }

    public void marquerInteresse(Evenement evenement, Utilisateur utilisateur) {
        if (!evenement.getInteresses().contains(utilisateur)) {
            evenement.getInteresses().add(utilisateur);
            evenementRepository.save(evenement);
        }
    }

    public void retirerInteresse(Evenement evenement, Utilisateur utilisateur) {
        if (evenement.getInteresses().contains(utilisateur)) {
            evenement.getInteresses().remove(utilisateur);
            evenementRepository.save(evenement);
        }
    }

    public boolean estInscrit(Evenement evenement, Utilisateur utilisateur) {

        return evenement.getInscrits().contains(utilisateur);
    }

    public boolean estInteresse(Evenement evenement, Utilisateur utilisateur) {

        return evenement.getInteresses().contains(utilisateur);
    }

    public void ajouterParticipant(Evenement evenement, Utilisateur utilisateur) {
    }

    public List<Evenement> getEvenementsOuUtilisateurEstInscrit(Long idUser) {
        List<Evenement> evenements = new ArrayList<>(evenementRepository.findByInscrits_IdUser(idUser));
        evenements.sort(Comparator.comparing(Evenement::getDateDebut));
        return evenements;
    }

    public List<Evenement> getEvenementsDesAutresUtilisateurs(Long idUser) {
        Utilisateur organisateur = new Utilisateur();
        organisateur.setIdUser(idUser);
        return evenementRepository.findByOrganisateurIsNot(organisateur.getIdUser());
    }

}
