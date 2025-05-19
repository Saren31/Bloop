package utcapitole.miage.bloop.model.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ReactionTest {

    @Test
    void testGettersAndSetters() {
        Reaction reaction = new Reaction();
        reaction.setId(1L);

        Post post = new Post();
        Utilisateur utilisateur = new Utilisateur();

        reaction.setPost(post);
        reaction.setUtilisateur(utilisateur);
        reaction.setLiked(true);
        reaction.setType("LIKE");

        assertThat(reaction.getId()).isEqualTo(1L);
        assertThat(reaction.getPost()).isEqualTo(post);
        assertThat(reaction.getUtilisateur()).isEqualTo(utilisateur);
        assertThat(reaction.isLiked()).isTrue();
        assertThat(reaction.getType()).isEqualTo("LIKE");
    }
}