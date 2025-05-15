package utcapitole.miage.bloop;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.model.entity.Utilisateur;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurTest {

    private static final String DEFAUT_NOM = "Dupont";
    private static final String DEFAUT_PRENOM = "Jean";
    private static final String DEFAUT_EMAIL = "jean.dupont@ut-capitole.fr";
    private static final String DEFAUT_MDP = "password";
    private static final String DEFAUT_PSEUDO = "jdupont";
    private static final byte[] DEFAUT_AVATAR = new byte[]{1, 2, 3};
    private static final String DEFAUT_TEL = "0123456789";
    private static final boolean DEFAUT_VISIBILITE = true;
    private static final boolean DEFAUT_VALIDATION = false;

    private static Utilisateur getUtilisateur() {
        final Utilisateur utilisateur = new Utilisateur();

        utilisateur.setNomUser(DEFAUT_NOM);
        utilisateur.setPrenomUser(DEFAUT_PRENOM);
        utilisateur.setEmailUser(DEFAUT_EMAIL);
        utilisateur.setMdpUser(DEFAUT_MDP);
        utilisateur.setPseudoUser(DEFAUT_PSEUDO);
        utilisateur.setAvatarUser(DEFAUT_AVATAR);
        utilisateur.setTelUser(DEFAUT_TEL);
        utilisateur.setVisibiliteUser(DEFAUT_VISIBILITE);
        utilisateur.setValiderInscription(DEFAUT_VALIDATION);
        return utilisateur;
    }

    @Test
    void testAllGettersAndSettersShouldWork() {
        final Utilisateur utilisateur = getUtilisateur();

        final String nom = utilisateur.getNomUser();
        final String prenom = utilisateur.getPrenomUser();
        final String email = utilisateur.getEmailUser();
        final String mdp = utilisateur.getMdpUser();
        final String pseudo = utilisateur.getPseudoUser();
        final byte[] avatar = utilisateur.getAvatarUser();
        final String tel = utilisateur.getTelUser();
        final boolean visibilite = utilisateur.isVisibiliteUser();
        final boolean validation = utilisateur.isValiderInscription();

        assertAll("Getters & Setters",
                () -> assertEquals(DEFAUT_NOM, nom),
                () -> assertEquals(DEFAUT_PRENOM, prenom),
                () -> assertEquals(DEFAUT_EMAIL, email),
                () -> assertEquals(DEFAUT_MDP, mdp),
                () -> assertEquals(DEFAUT_PSEUDO, pseudo),
                () -> assertArrayEquals(DEFAUT_AVATAR, avatar),
                () -> assertEquals(DEFAUT_TEL, tel),
                () -> assertEquals(DEFAUT_VISIBILITE, visibilite),
                () -> assertEquals(DEFAUT_VALIDATION, validation)
        );
    }

    @Test
    void testEmailFormatValide() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmailUser("test@ut-capitole.fr");
        assertTrue(utilisateur.getEmailUser().endsWith("@ut-capitole.fr"));
    }

    @Test
    void testEmailFormatInvalide() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmailUser("test@gmail.com");
        assertFalse(utilisateur.getEmailUser().endsWith("@ut-capitole.fr"));
    }

    @Test
    void testValidationInscription() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setValiderInscription(true);
        assertTrue(utilisateur.isValiderInscription());
    }

    @Test
    void testTokenInscription() {
        Utilisateur utilisateur = new Utilisateur();
        String token = UUID.randomUUID().toString();
        utilisateur.setTokenInscription(token);
        assertEquals(token, utilisateur.getTokenInscription());
    }

}
