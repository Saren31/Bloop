package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.repository.jpa.GroupeRepository;

/**
 * Service pour gérer les opérations liées aux groupes.
 */
@Service
public class GroupeService {

    private final GroupeRepository groupeRepository;

    /**
     * Constructeur pour injecter le repository des groupes.
     *
     * @param groupeRepository Le repository pour accéder aux données des groupes.
     */
    @Autowired
    public GroupeService(GroupeRepository groupeRepository) {
        this.groupeRepository = groupeRepository;
    }

    /**
     * Enregistre un groupe dans le repository.
     *
     * @param groupe Le groupe à enregistrer.
     * @return Une chaîne indiquant la vue à afficher après l'enregistrement.
     */
    public String enregistrerGroupe(Groupe groupe) {
        groupeRepository.save(groupe);
        return "accueil";
    }

    /**
     * Recherche un groupe par son identifiant.
     *
     * @param id L'identifiant du groupe à rechercher.
     * @return Le groupe correspondant à l'identifiant, ou null s'il n'existe pas.
     */
    public Groupe trouverGroupeParId(Long id) {
        return groupeRepository.findById(id).orElse(null);
    }
}