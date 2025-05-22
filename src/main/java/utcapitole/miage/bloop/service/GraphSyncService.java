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

/**
 * Service pour synchroniser les données des utilisateurs entre une base SQL et une base Neo4j.
 * Fournit une méthode pour copier tous les utilisateurs et leurs relations d'amitié
 * de la base SQL vers le graphe Neo4j.
 */
@Service
public class GraphSyncService {

    private final UtilisateurRepository utilisateurRepository; // JPA repo SQL
    private final UserGraphRepository userGraphRepository;     // Neo4j repo

    /**
     * Constructeur avec injection des repositories nécessaires.
     *
     * @param utilisateurRepository Repository JPA pour les entités Utilisateur.
     * @param userGraphRepository Repository Neo4j pour les nœuds UserNode.
     */
    public GraphSyncService(UtilisateurRepository utilisateurRepository, UserGraphRepository userGraphRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.userGraphRepository = userGraphRepository;
    }

    /**
     * Synchronise tous les utilisateurs et leurs relations d'amitié
     * de la base SQL vers la base Neo4j.
     * Cette méthode est transactionnelle pour garantir la cohérence des données.
     */
    @Transactional
    public void syncAllToGraph() {
        List<Utilisateur> allUsers = utilisateurRepository.findAll();

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