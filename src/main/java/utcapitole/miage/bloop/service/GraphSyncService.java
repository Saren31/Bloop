package utcapitole.miage.bloop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.model.node.UserNode;
import utcapitole.miage.bloop.repository.neo4j.UserGraphRepository;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GraphSyncService {

    private final UtilisateurRepository utilisateurRepository; // JPA repo SQL
    private final UserGraphRepository userGraphRepository;     // Neo4j repo

    public GraphSyncService(UtilisateurRepository utilisateurRepository, UserGraphRepository userGraphRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.userGraphRepository = userGraphRepository;
    }

    @Transactional
    public void syncAllToGraph() {
        // 1. Récupère tous les utilisateurs SQL
        List<Utilisateur> allUsers = utilisateurRepository.findAll();

        // 2. Insère les nœuds et relations dans Neo4j
        for (Utilisateur u : allUsers) {
            UserNode node = new UserNode();
            node.setId(u.getIdUser());
            node.setNom(u.getNomUser());
            Set<UserNode> amis = new HashSet<>();
            for (Utilisateur ami : u.getAmis()) {
                UserNode amiNode = new UserNode();
                amiNode.setId(ami.getIdUser());
                amiNode.setNom(ami.getNomUser());
                amis.add(amiNode);
            }
            node.setAmis(amis);
            userGraphRepository.save(node);
        }
    }
}
