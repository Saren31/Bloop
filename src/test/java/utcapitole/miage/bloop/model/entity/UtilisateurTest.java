package utcapitole.miage.bloop.model.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UtilisateurTest {

    @Test
    void testGettersAndSetters() {
        Utilisateur u = new Utilisateur();
        u.setNomUser("Nom");
        u.setPrenomUser("Prenom");
        u.setEmailUser("mail@test.com");
        u.setVisibiliteUser(true);

        assertThat(u.getNomUser()).isEqualTo("Nom");
        assertThat(u.getPrenomUser()).isEqualTo("Prenom");
        assertThat(u.getEmailUser()).isEqualTo("mail@test.com");
        assertThat(u.isVisibiliteUser()).isTrue();
    }

    @Test
    void testEqualsAndHashCode() {
        Utilisateur u1 = new Utilisateur();
        u1.setIdUser(1L);
        Utilisateur u2 = new Utilisateur();
        u2.setIdUser(1L);

        assertThat(u1)
                .isEqualTo(u2)
                .hasSameHashCodeAs(u2);
    }

    @Test
    void testToString() {
        Utilisateur u = new Utilisateur();
        u.setNomUser("Nom");
        assertThat(u.toString()).contains("Nom");
    }
}