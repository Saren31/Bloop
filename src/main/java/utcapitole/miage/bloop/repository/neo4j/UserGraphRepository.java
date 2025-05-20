package utcapitole.miage.bloop.repository.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import utcapitole.miage.bloop.dto.RecommendationDTO;
import utcapitole.miage.bloop.model.node.UserNode;

import java.util.List;

public interface UserGraphRepository extends Neo4jRepository<UserNode, Long> {

    @Query("""
    MATCH (me:User {id: $id})-[:AMI]->(:User)-[:AMI]->(reco:User)
    WHERE NOT (me)-[:AMI]->(reco) AND me <> reco
    RETURN reco.id AS id, count(*) AS nombreAmisCommuns
    ORDER BY nombreAmisCommuns DESC
    LIMIT 5
    """)
    List<RecommendationDTO> recommendFriends(Long id);
}