package utcapitole.miage.bloop.model.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {

    @Test
    void testGettersAndSetters() {
        Message m = new Message();
        m.setId(1L);
        Utilisateur u1 = new Utilisateur();
        Utilisateur u2 = new Utilisateur();
        m.setExpediteur(u1);
        m.setDestinataire(u2);
        m.setContenu("Salut");
        LocalDateTime now = LocalDateTime.now();
        m.setDateEnvoi(now);

        assertThat(m.getId()).isEqualTo(1L);
        assertThat(m.getExpediteur()).isEqualTo(u1);
        assertThat(m.getDestinataire()).isEqualTo(u2);
        assertThat(m.getContenu()).isEqualTo("Salut");
        assertThat(m.getDateEnvoi()).isEqualTo(now);
    }
}