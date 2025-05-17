package utcapitole.miage.bloop.model.entity;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

class GroupeTest {

    @Test
    void testGettersAndSetters() {
        Groupe g = new Groupe();
        g.setIdGroupe(5L);
        g.setNomGroupe("TestGroupe");
        byte[] logo = {4,5,6};
        g.setLogoGroupe(logo);
        g.setThemeGroupe("Sport");
        g.setDescriptionGroupe("Un groupe de test");

        Utilisateur createur = new Utilisateur();
        g.setCreateurGroupe(createur);

        var membres = new ArrayList<Utilisateur>();
        g.setMembres(membres);

        assertThat(g.getIdGroupe()).isEqualTo(5L);
        assertThat(g.getNomGroupe()).isEqualTo("TestGroupe");
        assertThat(g.getLogoGroupe()).isEqualTo(logo);
        assertThat(g.getThemeGroupe()).isEqualTo("Sport");
        assertThat(g.getDescriptionGroupe()).isEqualTo("Un groupe de test");
        assertThat(g.getCreateurGroupe()).isEqualTo(createur);
        assertThat(g.getMembres()).isEqualTo(membres);
    }

    @Test
    void testEqualsAndHashCode() {
        Groupe g1 = new Groupe();
        g1.setIdGroupe(1L);
        Groupe g2 = new Groupe();
        g2.setIdGroupe(1L);

        assertThat(g1)
                .isEqualTo(g2)
                .hasSameHashCodeAs(g2);
    }

    @Test
    void testToString() {
        Groupe g = new Groupe();
        g.setNomGroupe("TestGroupe");
        assertThat(g.toString()).contains("TestGroupe");
    }
}