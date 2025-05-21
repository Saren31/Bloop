package utcapitole.miage.bloop.repository.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import utcapitole.miage.bloop.dto.RecommendationDTO;
import utcapitole.miage.bloop.model.node.UserNode;

import java.util.List;

public interface UserGraphRepository extends Neo4jRepository<UserNode, Long> {

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