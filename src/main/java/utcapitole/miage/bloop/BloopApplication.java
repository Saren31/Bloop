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

import java.util.ArrayList;
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

			// Création de 10 utilisateurs pour avoir des recommandations intéressantes
			List<Utilisateur> utilisateurs = new ArrayList<>();

			Utilisateur u = new Utilisateur();
			u.setNomUser("Dupont");
			u.setPrenomUser("Jean");
			u.setEmailUser("email@ut-capitole.fr");
			u.setMdpUser(passwordEncoder.encode("test"));
			u.setPseudoUser("jdupont");
			u.setTelUser("0123456789");
			u.setVisibiliteUser(true);
			u.setValiderInscription(true);
			utilisateurs.add(u);

			// Création de plus d'utilisateurs pour des recommandations intéressantes
			String[] noms = {"Martin", "Bernard", "Thomas", "Petit", "Robert", "Richard", "Durand", "Dubois", "Moreau", "Simon"};
			String[] prenoms = {"Alice", "Bob", "Charlie", "David", "Emma", "François", "Gérard", "Hélène", "Isabelle", "Julie"};

			for (int i = 0; i < 10; i++) {
				Utilisateur user = new Utilisateur();
				user.setNomUser(noms[i]);
				user.setPrenomUser(prenoms[i]);
				user.setEmailUser(prenoms[i].toLowerCase() + "@ut-capitole.fr");
				user.setMdpUser(passwordEncoder.encode("test"));
				user.setPseudoUser(prenoms[i].toLowerCase() + i);
				user.setTelUser("010000000" + i);
				user.setVisibiliteUser(true);
				user.setValiderInscription(true);
				utilisateurs.add(user);
			}

			// Sauvegarde initiale pour générer les IDs
			utilisateurRepository.saveAll(utilisateurs);

			// Création d'un groupe
			Groupe groupeTest = new Groupe();
			groupeTest.setNomGroupe("Groupe de Test");
			groupeTest.setThemeGroupe("Test");
			groupeTest.setDescriptionGroupe("Ceci est un groupe de test.");
			groupeTest.setCreateurGroupe(utilisateurs.get(0));
			groupeRepository.save(groupeTest);

			// Ajout de membres au groupe
			groupeTest.getMembres().add(utilisateurs.get(0));
			utilisateurs.get(0).getGroupes().add(groupeTest);
			utilisateurs.get(1).getGroupes().add(groupeTest);
			utilisateurs.get(2).getGroupes().add(groupeTest);

			// Création d'un réseau d'amis en forme d'arbre pour générer des recommandations
			// Jean (0) est ami avec Alice (1) et Bob (2)
			utilisateurs.get(0).getAmis().add(utilisateurs.get(1));
			utilisateurs.get(0).getAmis().add(utilisateurs.get(2));
			utilisateurs.get(1).getAmis().add(utilisateurs.get(0));
			utilisateurs.get(2).getAmis().add(utilisateurs.get(0));

			// Alice (1) est amie avec Charlie (3) et David (4)
			utilisateurs.get(1).getAmis().add(utilisateurs.get(3));
			utilisateurs.get(1).getAmis().add(utilisateurs.get(4));
			utilisateurs.get(3).getAmis().add(utilisateurs.get(1));
			utilisateurs.get(4).getAmis().add(utilisateurs.get(1));

			// Bob (2) est ami avec Emma (5) et François (6)
			utilisateurs.get(2).getAmis().add(utilisateurs.get(5));
			utilisateurs.get(2).getAmis().add(utilisateurs.get(6));
			utilisateurs.get(5).getAmis().add(utilisateurs.get(2));
			utilisateurs.get(6).getAmis().add(utilisateurs.get(2));

			// Charlie (3) est ami avec Gérard (7)
			utilisateurs.get(3).getAmis().add(utilisateurs.get(7));
			utilisateurs.get(7).getAmis().add(utilisateurs.get(3));

			// David (4) est ami avec Hélène (8)
			utilisateurs.get(4).getAmis().add(utilisateurs.get(8));
			utilisateurs.get(8).getAmis().add(utilisateurs.get(4));

			// François (6) est ami avec Isabelle (9)
			utilisateurs.get(6).getAmis().add(utilisateurs.get(9));
			utilisateurs.get(9).getAmis().add(utilisateurs.get(6));

			// Sauvegarde finale pour persister les relations d'amitié
			utilisateurRepository.saveAll(utilisateurs);
			groupeRepository.save(groupeTest);
		};
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner syncGraphOnStartup(GraphSyncService graphSyncService) {
		return args -> graphSyncService.syncAllToGraph();
	}
}