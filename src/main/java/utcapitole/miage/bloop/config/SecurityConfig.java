package utcapitole.miage.bloop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import utcapitole.miage.bloop.service.CustomUserDetailsService;

/**
 * Configuration de la sécurité de l'application avec Spring Security.
 * Cette classe configure les filtres de sécurité, la gestion des utilisateurs,
 * et les encodeurs de mot de passe.
 */
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructeur pour injecter le service de gestion des utilisateurs personnalisés.
     *
     * @param userDetailsService Service personnalisé pour la gestion des utilisateurs.
     */
    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configure la chaîne de filtres de sécurité.
     *
     * @param http L'objet HttpSecurity utilisé pour configurer les règles de sécurité.
     * @return La chaîne de filtres de sécurité configurée.
     * @throws Exception En cas d'erreur lors de la configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/",                       // Page racine.
                                "/auth/login",            // Page de connexion personnalisée.
                                "/auth/register",         // Formulaire d'inscription.
                                "/auth/register_user",    // Soumission du formulaire d'inscription.
                                "/confirm**",// Confirmation par e-mail.
                                "/relations/**",
                                "/evenement/**",
                                "/css/**", "/js/**", "/images/**" // Ressources statiques.
                        ).permitAll()                   // Autorise l'accès sans authentification.
                        .anyRequest().authenticated()   // Requiert une authentification pour toutes les autres requêtes.
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")       // Page personnalisée de connexion.
                        .defaultSuccessUrl("/accueil", true) // Redirection après connexion réussie.
                        .permitAll()                    // Autorise l'accès à la page de connexion.
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")           // URL de déconnexion.
                        .logoutSuccessUrl("/auth/login?logout") // Redirection après déconnexion.
                        .permitAll()                    // Autorise l'accès à la déconnexion.
                );

        http.userDetailsService(userDetailsService);     // Utilise le service personnalisé pour la gestion des utilisateurs.

        return http.build();
    }

    /**
     * Définit un encodeur de mot de passe basé sur BCrypt.
     *
     * @return Un encodeur de mot de passe BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure le gestionnaire d'authentification avec le service utilisateur et l'encodeur de mot de passe.
     *
     * @param http L'objet HttpSecurity utilisé pour configurer le gestionnaire d'authentification.
     * @return Le gestionnaire d'authentification configuré.
     * @throws Exception En cas d'erreur lors de la configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .userDetailsService(userDetailsService) // Configure le service utilisateur personnalisé.
                .passwordEncoder(passwordEncoder());    // Configure l'encodeur de mot de passe.

        return authBuilder.build();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

}