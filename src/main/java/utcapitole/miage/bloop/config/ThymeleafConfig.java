package utcapitole.miage.bloop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

/**
 * Configuration pour Thymeleaf.
 * Cette classe configure les extensions nécessaires pour intégrer Spring Security avec Thymeleaf.
 */
@Configuration
public class ThymeleafConfig {

    /**
     * Définit un bean pour le dialecte Spring Security de Thymeleaf.
     * Ce dialecte permet d'utiliser des balises spécifiques à Spring Security
     * dans les templates Thymeleaf, comme les contrôles d'accès.
     *
     * @return une instance de SpringSecurityDialect
     */
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}