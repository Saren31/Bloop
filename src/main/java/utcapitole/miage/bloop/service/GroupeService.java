package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.repository.GroupeRepository;

@Service
public class GroupeService {

    private final GroupeRepository groupeRepository;

    @Autowired
    public GroupeService(GroupeRepository groupeRepository) {
        this.groupeRepository = groupeRepository;
    }

    public String enregistrerGroupe(Groupe groupe) {
        groupeRepository.save(groupe);
        return "accueil";
    }
}
