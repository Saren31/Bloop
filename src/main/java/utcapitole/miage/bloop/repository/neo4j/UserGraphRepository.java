package utcapitole.miage.bloop.repository.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import utcapitole.miage.bloop.dto.RecommendationDTO;
import utcapitole.miage.bloop.model.node.UserNode;

import java.util.List;

/**
 * Repository Neo4j pour la gestion des utilisateurs dans le graphe.
 * Fournit des méthodes personnalisées pour la recommandation d'amis.
 */
public interface UserGraphRepository extends Neo4jRepository<UserNode, Long> {

    /**
     * Recommande des amis à un utilisateur donné.
     * Cette méthode recherche des utilisateurs connectés via 2 à 4 relations d'amitié,
     * exclut les amis directs et l'utilisateur lui-même, puis compte le nombre d'amis communs.
     * Elle retourne les 5 recommandations les plus pertinentes.
     *
     * @param id l'identifiant de l'utilisateur pour lequel on souhaite obtenir des recommandations
     * @return une liste de RecommendationDTO contenant l'id de l'utilisateur recommandé et le nombre d'amis communs
     */
    @Query("""
    CALL {
        WITH $id AS userId
        MATCH (me:User {id: userId})
        MATCH path = (me)-[:AMI*2..4]->(reco:User)
        WHERE NOT (me)-[:AMI]->(reco) AND me <> reco
        WITH reco, length(path) AS distance, count(*) AS nombreAmisCommuns
        RETURN reco.id AS id, distance, nombreAmisCommuns
        ORDER BY distance, nombreAmisCommuns DESC
        LIMIT 5
    }
    RETURN id, nombreAmisCommuns
    """)
    List<RecommendationDTO> recommendFriends(Long id);
}