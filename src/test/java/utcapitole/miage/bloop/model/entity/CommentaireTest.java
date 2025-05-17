package utcapitole.miage.bloop.model.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class CommentaireTest {

    @Test
    void testGettersAndSetters() {
        Commentaire c = new Commentaire();
        c.setId(10L);
        c.setTexte("Hello");
        Date d = new Date();
        c.setDateCommentaire(d);
        Utilisateur u = new Utilisateur();
        Post p = new Post();
        c.setUtilisateur(u);
        c.setPost(p);

        assertThat(c.getId()).isEqualTo(10L);
        assertThat(c.getTexte()).isEqualTo("Hello");
        assertThat(c.getDateCommentaire()).isEqualTo(d);
        assertThat(c.getUtilisateur()).isEqualTo(u);
        assertThat(c.getPost()).isEqualTo(p);
    }

}