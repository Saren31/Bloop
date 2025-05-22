package utcapitole.miage.bloop.model.node;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

/**
 * Représente un nœud utilisateur dans la base de données Neo4j.
 * Ce nœud contient les informations de l'utilisateur ainsi que ses relations d'amitié.
 */
@Node("User")
public class UserNode {

    /**
     * Identifiant unique de l'utilisateur.
     */
    @Id
    private Long id;

    /**
     * Nom de l'utilisateur.
     */
    private String nom;

    /**
     * Ensemble des amis de l'utilisateur.
     * Relation de type "AMI" avec une direction sortante.
     */
    @Relationship(type = "AMI", direction = Relationship.Direction.OUTGOING)
    private Set<UserNode> amis;

    /**
     * Récupère l'identifiant unique de l'utilisateur.
     *
     * @return L'identifiant de l'utilisateur.
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique de l'utilisateur.
     *
     * @param id L'identifiant de l'utilisateur.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Récupère le nom de l'utilisateur.
     *
     * @return Le nom de l'utilisateur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'utilisateur.
     *
     * @param nom Le nom de l'utilisateur.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère l'ensemble des amis de l'utilisateur.
     *
     * @return Les amis de l'utilisateur.
     */
    public Set<UserNode> getAmis() {
        return amis;
    }

    /**
     * Définit l'ensemble des amis de l'utilisateur.
     *
     * @param amis Les amis de l'utilisateur.
     */
    public void setAmis(Set<UserNode> amis) {
        this.amis = amis;
    }
}