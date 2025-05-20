package utcapitole.miage.bloop.model.node;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Node("User")
public class UserNode {
    @Id
    private Long id;
    private String nom;

    @Relationship(type = "AMI", direction = Relationship.Direction.OUTGOING)
    private Set<UserNode> amis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<UserNode> getAmis() {
        return amis;
    }

    public void setAmis(Set<UserNode> amis) {
        this.amis = amis;
    }
}