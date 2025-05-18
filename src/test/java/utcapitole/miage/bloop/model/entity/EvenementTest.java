package utcapitole.miage.bloop.model.entity;

import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class EvenementTest {

    @Test
    void testGettersAndSetters() {
        Evenement e = new Evenement();
        e.setId(10L);
        e.setTitre("Titre");
        e.setDescription("Desc");
        Date debut = new Date();
        Date fin = new Date();
        e.setDateDebut(debut);
        e.setDateFin(fin);
        e.setLieu("Toulouse");
        Utilisateur u = new Utilisateur();
        e.setOrganisateur(u);

        assertThat(e.getId()).isEqualTo(10L);
        assertThat(e.getTitre()).isEqualTo("Titre");
        assertThat(e.getDescription()).isEqualTo("Desc");
        assertThat(e.getDateDebut()).isEqualTo(debut);
        assertThat(e.getDateFin()).isEqualTo(fin);
        assertThat(e.getLieu()).isEqualTo("Toulouse");
        assertThat(e.getOrganisateur()).isEqualTo(u);
    }
}