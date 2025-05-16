package utcapitole.miage.bloop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utcapitole.miage.bloop.model.entity.Utilisateur;

/**
 * Interface UtilisateurRepository
 * Fournit des méthodes pour interagir avec la base de données des utilisateurs.
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    /**
     * Recherche un utilisateur par son jeton d'inscription.
     *
     * @param token Le jeton d'inscription de l'utilisateur.
     * @return L'utilisateur correspondant au jeton, ou null s'il n'existe pas.
     */
    Utilisateur findByTokenInscription(String token);

    /**
     * Recherche un utilisateur par son adresse e-mail.
     *
     * @param emailUser L'adresse e-mail de l'utilisateur.
     * @return L'utilisateur correspondant à l'adresse e-mail, ou null s'il n'existe pas.
     */
    Utilisateur findByEmailUser(String emailUser);

}