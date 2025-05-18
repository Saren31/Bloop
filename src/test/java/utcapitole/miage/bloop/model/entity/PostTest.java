package utcapitole.miage.bloop.model.entity;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void testGettersAndSetters() {
        Post post = new Post();
        post.setIdPost(10L);
        post.setTextePost("Hello");
        byte[] img = {1,2,3};
        post.setImagePost(img);
        Date now = new Date();
        post.setDatePost(now);

        Utilisateur u = new Utilisateur();
        post.setUtilisateur(u);

        assertThat(post.getIdPost()).isEqualTo(10L);
        assertThat(post.getTextePost()).isEqualTo("Hello");
        assertThat(post.getImagePost()).isEqualTo(img);
        assertThat(post.getDatePost()).isEqualTo(now);
        assertThat(post.getUtilisateur()).isEqualTo(u);
    }

    @Test
    void testEqualsAndHashCode() {
        Post p1 = new Post();
        p1.setIdPost(1L);
        Post p2 = new Post();
        p2.setIdPost(1L);

        assertThat(p1)
                .isEqualTo(p2)
                .hasSameHashCodeAs(p2);
    }
}