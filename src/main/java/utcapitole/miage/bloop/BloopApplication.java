package utcapitole.miage.bloop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.GroupeRepository;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;
import utcapitole.miage.bloop.service.GraphSyncService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Classe principale de l'application Spring Boot.
 */
@EnableJpaRepositories(basePackages = "utcapitole.miage.bloop.repository.jpa")
@EnableNeo4jRepositories(basePackages = "utcapitole.miage.bloop.repository.neo4j")
@SpringBootApplication
public class BloopApplication {

	/**
	 * Point d'entrée principal de l'application.
	 *
	 * @param args Les arguments de ligne de commande.
	 */
	public static void main(String[] args) {
		SpringApplication.run(BloopApplication.class, args);
	}

	/**
	 * Initialise les utilisateurs dans la base de données au démarrage de l'application.
	 * Ce bean est exécuté uniquement si le profil actif n'est pas "test".
	 *
	 * @param utilisateurRepository Le dépôt pour interagir avec les utilisateurs.
	 * @param passwordEncoder       L'encodeur de mots de passe pour sécuriser les mots de passe.
	 * @return Une instance de CommandLineRunner qui initialise les utilisateurs.
	 */
	@Bean
	@Profile("!test")
	public CommandLineRunner initParticipant(UtilisateurRepository utilisateurRepository,
											 GroupeRepository groupeRepository,
											 PasswordEncoder passwordEncoder) {
		return args -> {
			utilisateurRepository.deleteAll();
			groupeRepository.deleteAll();

			// Chargement de l'image
			byte[] avatar = Files.readAllBytes(Paths.get("src/main/resources/static/img/default-avatar.png"));

			// Création manuelle de 30 utilisateurs uniques
			Utilisateur u1 = new Utilisateur();
			u1.setNomUser("Veslin"); u1.setPrenomUser("dupont"); u1.setEmailUser("email@ut-capitole.fr");
			u1.setMdpUser(passwordEncoder.encode("test")); u1.setPseudoUser("jean");
			u1.setTelUser("0601020304"); u1.setVisibiliteUser(true); u1.setValiderInscription(true); u1.setAvatarUser(avatar);

			Utilisateur u2 = new Utilisateur();
			u2.setNomUser("Martin"); u2.setPrenomUser("Alice"); u2.setEmailUser("alice@ut-capitole.fr");
			u2.setMdpUser(passwordEncoder.encode("test")); u2.setPseudoUser("amartin");
			u2.setTelUser("0607080910"); u2.setVisibiliteUser(true); u2.setValiderInscription(true); u2.setAvatarUser(avatar);

			Utilisateur u3 = new Utilisateur();
			u3.setNomUser("Bernard"); u3.setPrenomUser("Thomas"); u3.setEmailUser("thomas.bernard@ut-capitole.fr");
			u3.setMdpUser(passwordEncoder.encode("test")); u3.setPseudoUser("tbernard");
			u3.setTelUser("0611121314"); u3.setVisibiliteUser(true); u3.setValiderInscription(true); u3.setAvatarUser(avatar);

			Utilisateur u4 = new Utilisateur();
			u4.setNomUser("Petit"); u4.setPrenomUser("Emma"); u4.setEmailUser("emma.petit@ut-capitole.fr");
			u4.setMdpUser(passwordEncoder.encode("test")); u4.setPseudoUser("epetit");
			u4.setTelUser("0615161718"); u4.setVisibiliteUser(true); u4.setValiderInscription(true); u4.setAvatarUser(avatar);

			Utilisateur u5 = new Utilisateur();
			u5.setNomUser("Durand"); u5.setPrenomUser("Lucas"); u5.setEmailUser("lucas.durand@ut-capitole.fr");
			u5.setMdpUser(passwordEncoder.encode("test")); u5.setPseudoUser("ldurand");
			u5.setTelUser("0619202122"); u5.setVisibiliteUser(true); u5.setValiderInscription(true); u5.setAvatarUser(avatar);

			Utilisateur u6 = new Utilisateur();
			u6.setNomUser("Leroy"); u6.setPrenomUser("Chloé"); u6.setEmailUser("chloe.leroy@ut-capitole.fr");
			u6.setMdpUser(passwordEncoder.encode("test")); u6.setPseudoUser("cleroy");
			u6.setTelUser("0623242526"); u6.setVisibiliteUser(true); u6.setValiderInscription(true); u6.setAvatarUser(avatar);

			Utilisateur u7 = new Utilisateur();
			u7.setNomUser("Moreau"); u7.setPrenomUser("Maxime"); u7.setEmailUser("maxime.moreau@ut-capitole.fr");
			u7.setMdpUser(passwordEncoder.encode("test")); u7.setPseudoUser("mmoreau");
			u7.setTelUser("0627282930"); u7.setVisibiliteUser(true); u7.setValiderInscription(true); u7.setAvatarUser(avatar);

			Utilisateur u8 = new Utilisateur();
			u8.setNomUser("Simon"); u8.setPrenomUser("Léa"); u8.setEmailUser("lea.simon@ut-capitole.fr");
			u8.setMdpUser(passwordEncoder.encode("test")); u8.setPseudoUser("lsimon");
			u8.setTelUser("0631323334"); u8.setVisibiliteUser(true); u8.setValiderInscription(true); u8.setAvatarUser(avatar);

			Utilisateur u9 = new Utilisateur();
			u9.setNomUser("Laurent"); u9.setPrenomUser("Hugo"); u9.setEmailUser("hugo.laurent@ut-capitole.fr");
			u9.setMdpUser(passwordEncoder.encode("test")); u9.setPseudoUser("hlaurent");
			u9.setTelUser("0635363738"); u9.setVisibiliteUser(true); u9.setValiderInscription(true); u9.setAvatarUser(avatar);

			Utilisateur u10 = new Utilisateur();
			u10.setNomUser("Lefebvre"); u10.setPrenomUser("Manon"); u10.setEmailUser("manon.lefebvre@ut-capitole.fr");
			u10.setMdpUser(passwordEncoder.encode("test")); u10.setPseudoUser("mlefebvre");
			u10.setTelUser("0639404142"); u10.setVisibiliteUser(true); u10.setValiderInscription(true); u10.setAvatarUser(avatar);

			Utilisateur u11 = new Utilisateur();
			u11.setNomUser("Mercier"); u11.setPrenomUser("Nathan"); u11.setEmailUser("nathan.mercier@ut-capitole.fr");
			u11.setMdpUser(passwordEncoder.encode("test")); u11.setPseudoUser("nmercier");
			u11.setTelUser("0643444546"); u11.setVisibiliteUser(true); u11.setValiderInscription(true); u11.setAvatarUser(avatar);

			Utilisateur u12 = new Utilisateur();
			u12.setNomUser("Blanc"); u12.setPrenomUser("Camille"); u12.setEmailUser("camille.blanc@ut-capitole.fr");
			u12.setMdpUser(passwordEncoder.encode("test")); u12.setPseudoUser("cblanc");
			u12.setTelUser("0647484950"); u12.setVisibiliteUser(true); u12.setValiderInscription(true); u12.setAvatarUser(avatar);

			Utilisateur u13 = new Utilisateur();
			u13.setNomUser("Faure"); u13.setPrenomUser("Théo"); u13.setEmailUser("theo.faure@ut-capitole.fr");
			u13.setMdpUser(passwordEncoder.encode("test")); u13.setPseudoUser("tfaure");
			u13.setTelUser("0651525354"); u13.setVisibiliteUser(true); u13.setValiderInscription(true); u13.setAvatarUser(avatar);

			Utilisateur u14 = new Utilisateur();
			u14.setNomUser("Rousseau"); u14.setPrenomUser("Jade"); u14.setEmailUser("jade.rousseau@ut-capitole.fr");
			u14.setMdpUser(passwordEncoder.encode("test")); u14.setPseudoUser("jrousseau");
			u14.setTelUser("0655565758"); u14.setVisibiliteUser(true); u14.setValiderInscription(true); u14.setAvatarUser(avatar);

			Utilisateur u15 = new Utilisateur();
			u15.setNomUser("Vincent"); u15.setPrenomUser("Louis"); u15.setEmailUser("louis.vincent@ut-capitole.fr");
			u15.setMdpUser(passwordEncoder.encode("test")); u15.setPseudoUser("lvincent");
			u15.setTelUser("0659606162"); u15.setVisibiliteUser(true); u15.setValiderInscription(true); u15.setAvatarUser(avatar);

			Utilisateur u16 = new Utilisateur();
			u16.setNomUser("Roux"); u16.setPrenomUser("Eva"); u16.setEmailUser("eva.roux@ut-capitole.fr");
			u16.setMdpUser(passwordEncoder.encode("test")); u16.setPseudoUser("eroux");
			u16.setTelUser("0663646566"); u16.setVisibiliteUser(true); u16.setValiderInscription(true); u16.setAvatarUser(avatar);

			Utilisateur u17 = new Utilisateur();
			u17.setNomUser("Fournier"); u17.setPrenomUser("Jules"); u17.setEmailUser("jules.fournier@ut-capitole.fr");
			u17.setMdpUser(passwordEncoder.encode("test")); u17.setPseudoUser("jfournier");
			u17.setTelUser("0667686970"); u17.setVisibiliteUser(true); u17.setValiderInscription(true); u17.setAvatarUser(avatar);

			Utilisateur u18 = new Utilisateur();
			u18.setNomUser("Girard"); u18.setPrenomUser("Clara"); u18.setEmailUser("clara.girard@ut-capitole.fr");
			u18.setMdpUser(passwordEncoder.encode("test")); u18.setPseudoUser("cgirard");
			u18.setTelUser("0671727374"); u18.setVisibiliteUser(true); u18.setValiderInscription(true); u18.setAvatarUser(avatar);

			Utilisateur u19 = new Utilisateur();
			u19.setNomUser("Robin"); u19.setPrenomUser("Adam"); u19.setEmailUser("adam.robin@ut-capitole.fr");
			u19.setMdpUser(passwordEncoder.encode("test")); u19.setPseudoUser("arobin");
			u19.setTelUser("0675767778"); u19.setVisibiliteUser(true); u19.setValiderInscription(true); u19.setAvatarUser(avatar);

			Utilisateur u20 = new Utilisateur();
			u20.setNomUser("Roy"); u20.setPrenomUser("Lucie"); u20.setEmailUser("lucie.roy@ut-capitole.fr");
			u20.setMdpUser(passwordEncoder.encode("test")); u20.setPseudoUser("lroy");
			u20.setTelUser("0679808182"); u20.setVisibiliteUser(true); u20.setValiderInscription(true); u20.setAvatarUser(avatar);

			Utilisateur u21 = new Utilisateur();
			u21.setNomUser("Morel"); u21.setPrenomUser("Raphaël"); u21.setEmailUser("raphael.morel@ut-capitole.fr");
			u21.setMdpUser(passwordEncoder.encode("test")); u21.setPseudoUser("rmorel");
			u21.setTelUser("0683848586"); u21.setVisibiliteUser(true); u21.setValiderInscription(true); u21.setAvatarUser(avatar);

			Utilisateur u22 = new Utilisateur();
			u22.setNomUser("David"); u22.setPrenomUser("Sarah"); u22.setEmailUser("sarah.david@ut-capitole.fr");
			u22.setMdpUser(passwordEncoder.encode("test")); u22.setPseudoUser("sdavid");
			u22.setTelUser("0687888990"); u22.setVisibiliteUser(true); u22.setValiderInscription(true); u22.setAvatarUser(avatar);

			Utilisateur u23 = new Utilisateur();
			u23.setNomUser("Bertrand"); u23.setPrenomUser("Gabriel"); u23.setEmailUser("gabriel.bertrand@ut-capitole.fr");
			u23.setMdpUser(passwordEncoder.encode("test")); u23.setPseudoUser("gbertrand");
			u23.setTelUser("0691929394"); u23.setVisibiliteUser(true); u23.setValiderInscription(true); u23.setAvatarUser(avatar);

			Utilisateur u24 = new Utilisateur();
			u24.setNomUser("Gautier"); u24.setPrenomUser("Inès"); u24.setEmailUser("ines.gautier@ut-capitole.fr");
			u24.setMdpUser(passwordEncoder.encode("test")); u24.setPseudoUser("igautier");
			u24.setTelUser("0695969798"); u24.setVisibiliteUser(true); u24.setValiderInscription(true); u24.setAvatarUser(avatar);

			Utilisateur u25 = new Utilisateur();
			u25.setNomUser("Chevalier"); u25.setPrenomUser("Paul"); u25.setEmailUser("paul.chevalier@ut-capitole.fr");
			u25.setMdpUser(passwordEncoder.encode("test")); u25.setPseudoUser("pchevalier");
			u25.setTelUser("0700010203"); u25.setVisibiliteUser(true); u25.setValiderInscription(true); u25.setAvatarUser(avatar);

			Utilisateur u26 = new Utilisateur();
			u26.setNomUser("Perrin"); u26.setPrenomUser("Charlotte"); u26.setEmailUser("charlotte.perrin@ut-capitole.fr");
			u26.setMdpUser(passwordEncoder.encode("test")); u26.setPseudoUser("cperrin");
			u26.setTelUser("0704050607"); u26.setVisibiliteUser(true); u26.setValiderInscription(true); u26.setAvatarUser(avatar);

			Utilisateur u27 = new Utilisateur();
			u27.setNomUser("Clément"); u27.setPrenomUser("Arthur"); u27.setEmailUser("arthur.clement@ut-capitole.fr");
			u27.setMdpUser(passwordEncoder.encode("test")); u27.setPseudoUser("aclement");
			u27.setTelUser("0708091011"); u27.setVisibiliteUser(true); u27.setValiderInscription(true); u27.setAvatarUser(avatar);

			Utilisateur u28 = new Utilisateur();
			u28.setNomUser("Mathieu"); u28.setPrenomUser("Julie"); u28.setEmailUser("julie.mathieu@ut-capitole.fr");
			u28.setMdpUser(passwordEncoder.encode("test")); u28.setPseudoUser("jmathieu");
			u28.setTelUser("0712131415"); u28.setVisibiliteUser(true); u28.setValiderInscription(true); u28.setAvatarUser(avatar);

			Utilisateur u29 = new Utilisateur();
			u29.setNomUser("Marchand"); u29.setPrenomUser("Antoine"); u29.setEmailUser("antoine.marchand@ut-capitole.fr");
			u29.setMdpUser(passwordEncoder.encode("test")); u29.setPseudoUser("amarchand");
			u29.setTelUser("0716171819"); u29.setVisibiliteUser(true); u29.setValiderInscription(true); u29.setAvatarUser(avatar);

			Utilisateur u30 = new Utilisateur();
			u30.setNomUser("Dubois"); u30.setPrenomUser("Zoé"); u30.setEmailUser("zoe.dubois@ut-capitole.fr");
			u30.setMdpUser(passwordEncoder.encode("test")); u30.setPseudoUser("zdubois");
			u30.setTelUser("0720212223"); u30.setVisibiliteUser(true); u30.setValiderInscription(true); u30.setAvatarUser(avatar);

			// Ajoute tous les utilisateurs à la liste
			List<Utilisateur> utilisateurs = List.of(
					u1, u2, u3, u4, u5, u6, u7, u8, u9, u10,
					u11, u12, u13, u14, u15, u16, u17, u18, u19, u20,
					u21, u22, u23, u24, u25, u26, u27, u28, u29, u30
			);

			utilisateurRepository.saveAll(utilisateurs);

			// Création de liens d'amitié variés
			// Groupe d'amis 1
			u1.getAmis().add(u2); u2.getAmis().add(u1);
			u1.getAmis().add(u3); u3.getAmis().add(u1);
			u1.getAmis().add(u4); u4.getAmis().add(u1);
			u2.getAmis().add(u5); u5.getAmis().add(u2);
			u3.getAmis().add(u6); u6.getAmis().add(u3);
			u4.getAmis().add(u7); u7.getAmis().add(u4);
			u5.getAmis().add(u8); u8.getAmis().add(u5);

			// Groupe d'amis 2
			u9.getAmis().add(u10); u10.getAmis().add(u9);
			u9.getAmis().add(u11); u11.getAmis().add(u9);
			u10.getAmis().add(u12); u12.getAmis().add(u10);
			u11.getAmis().add(u13); u13.getAmis().add(u11);
			u12.getAmis().add(u14); u14.getAmis().add(u12);

			// Groupe d'amis 3
			u15.getAmis().add(u16); u16.getAmis().add(u15);
			u15.getAmis().add(u17); u17.getAmis().add(u15);
			u15.getAmis().add(u18); u18.getAmis().add(u15);
			u16.getAmis().add(u19); u19.getAmis().add(u16);
			u17.getAmis().add(u20); u20.getAmis().add(u17);

			// Groupe d'amis 4
			u21.getAmis().add(u22); u22.getAmis().add(u21);
			u21.getAmis().add(u23); u23.getAmis().add(u21);
			u22.getAmis().add(u24); u24.getAmis().add(u22);
			u23.getAmis().add(u25); u25.getAmis().add(u23);

			// Groupe d'amis 5
			u26.getAmis().add(u27); u27.getAmis().add(u26);
			u26.getAmis().add(u28); u28.getAmis().add(u26);
			u27.getAmis().add(u29); u29.getAmis().add(u27);
			u28.getAmis().add(u30); u30.getAmis().add(u28);

			// Connexions entre groupes
			u4.getAmis().add(u15); u15.getAmis().add(u4);
			u8.getAmis().add(u21); u21.getAmis().add(u8);
			u14.getAmis().add(u26); u26.getAmis().add(u14);
			u20.getAmis().add(u9); u9.getAmis().add(u20);
			u25.getAmis().add(u1); u1.getAmis().add(u25);

			utilisateurRepository.saveAll(utilisateurs);

			// Création d'un groupe
			Groupe groupe1 = new Groupe();
			groupe1.setNomGroupe("Étudiants MIAGE");
			groupe1.setThemeGroupe("Études");
			groupe1.setDescriptionGroupe("Groupe des étudiants en MIAGE de l'UT Capitole");
			groupe1.setCreateurGroupe(u1);
			groupeRepository.save(groupe1);

			// Ajout des membres au groupe
			groupe1.getMembres().add(u1);
			groupe1.getMembres().add(u2);
			groupe1.getMembres().add(u3);
			groupe1.getMembres().add(u4);
			groupe1.getMembres().add(u5);

			u1.getGroupes().add(groupe1);
			u2.getGroupes().add(groupe1);
			u3.getGroupes().add(groupe1);
			u4.getGroupes().add(groupe1);
			u5.getGroupes().add(groupe1);

			groupeRepository.save(groupe1);
			utilisateurRepository.saveAll(List.of(u1, u2, u3, u4, u5));
		};
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner syncGraphOnStartup(GraphSyncService graphSyncService) {
		return args -> graphSyncService.syncAllToGraph();
	}
}