package utcapitole.miage.bloop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

@SpringBootApplication
public class BloopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloopApplication.class, args);
	}

	@Bean
	CommandLineRunner initParticipant(UtilisateurRepository utilisateurRepository){
		return args -> {

			utilisateurRepository.deleteAll();

			Utilisateur u = new Utilisateur();
			u.setNomUser("Dupont");
			u.setEmailUser("email@ut-capitole.fr");
			u.setMdpUser("fdd");
			u.setVisibiliteUser(true);
			u.setValiderInscription(false);
			utilisateurRepository.save(u);
		};
	}
}
