package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Evenement;

import java.util.List;

@Service
public class EvenementService {

    private final EvenementRepository evenementRepository;

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
        if (!evenement.getParticipants().contains(utilisateur)) {
            evenement.getParticipants().add(utilisateur);
            evenementRepository.save(evenement);
        }
    }

    public void retirerUtilisateur(Evenement evenement, Utilisateur utilisateur) {
        if (evenement.getParticipants().contains(utilisateur)) {
            evenement.getParticipants().remove(utilisateur);
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
}
