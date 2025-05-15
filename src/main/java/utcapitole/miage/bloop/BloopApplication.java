package utcapitole.miage.bloop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.List;

/**
 * Classe principale de l'application Spring Boot.
 */
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
											 PasswordEncoder passwordEncoder) {
		return args -> {
			// Supprime tous les utilisateurs existants
			utilisateurRepository.deleteAll();

			// Création et configuration d'un utilisateur validé
			Utilisateur u = new Utilisateur();
			u.setNomUser("Dupont");
			u.setPrenomUser("Jean");
			u.setEmailUser("email@ut-capitole.fr");
			u.setMdpUser(passwordEncoder.encode("test"));
			u.setPseudoUser("jdupont");
			u.setTelUser("0123456789");
			u.setVisibiliteUser(true);
			u.setValiderInscription(true);

			// Création et configuration d'un deuxième utilisateur
			Utilisateur u1 = new Utilisateur();
			u1.setNomUser("Alice");
			u1.setPrenomUser("Test");
			u1.setEmailUser("alice@ut-capitole.fr");
			u1.setMdpUser("123");
			u1.setPseudoUser("alice");
			u1.setTelUser("0000000001");
			u1.setVisibiliteUser(true);

			// Création et configuration d'un troisième utilisateur
			Utilisateur u2 = new Utilisateur();
			u2.setNomUser("Bob");
			u2.setPrenomUser("Test");
			u2.setEmailUser("bob@ut-capitole.fr");
			u2.setMdpUser("123");
			u2.setPseudoUser("bob");
			u2.setTelUser("0000000002");
			u2.setVisibiliteUser(true);

			// Sauvegarde des utilisateurs dans la base de données
			utilisateurRepository.saveAll(List.of(u, u1, u2));
		};
	}
}