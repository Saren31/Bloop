package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

/**
 * Service personnalisé pour charger les détails d'un utilisateur.
 * Implémente l'interface UserDetailsService de Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur pour injecter le dépôt des utilisateurs.
     *
     * @param utilisateurRepository Le dépôt pour interagir avec les utilisateurs.
     */
    @Autowired
    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Charge un utilisateur par son adresse e-mail.
     *
     * @param email L'adresse e-mail de l'utilisateur à charger.
     * @return Les détails de l'utilisateur sous forme d'objet UserDetails.
     * @throws UsernameNotFoundException Si aucun utilisateur n'est trouvé avec l'adresse e-mail donnée.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmailUser(email);
        if (utilisateur == null) {
            throw new UsernameNotFoundException("Utilisateur introuvable : " + email);
        }
        return utilisateur;
    }
}