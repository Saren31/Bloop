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
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    /**
     * Constructeur pour injecter le service de gestion des utilisateurs personnalisés
     * et le gestionnaire d'échec d'authentification.
     *
     * @param userDetailsService Service personnalisé pour la gestion des utilisateurs.
     * @param authenticationFailureHandler Gestionnaire personnalisé pour les échecs d'authentification.
     */
    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, CustomAuthenticationFailureHandler authenticationFailureHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationFailureHandler = authenticationFailureHandler;
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
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/",
                                "/auth/login",
                                "/auth/register",
                                "/auth/register_user",
                                "/confirm**",
                                "/relations/**",
                                "/evenement/**",
                                "/error",
                                "/css/**", "/js/**", "/img/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/accueil", true)
                        .failureHandler(authenticationFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll()
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

    /**
     * Définit un filtre pour gérer les méthodes HTTP cachées (comme PUT ou DELETE).
     *
     * @return Une instance de HiddenHttpMethodFilter.
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

}