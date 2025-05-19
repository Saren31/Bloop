package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.repository.EvenementRepository;

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
}
