package utcapitole.miage.bloop.config;

import jakarta.persistence.EntityManagerFactory;
import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;

/**
 * Configuration des gestionnaires de transactions pour JPA et Neo4j.
 * Cette configuration est activée pour tous les profils sauf "test".
 */
@Configuration
@Profile("!test")
public class JpaTransactionManagerConfig {

    /**
     * Définit un bean pour le gestionnaire de transactions Neo4j.
     *
     * @param driver le driver Neo4j utilisé pour établir les connexions
     * @return une instance de Neo4jTransactionManager
     */
    @Bean(name = "neo4jTransactionManager")
    public Neo4jTransactionManager neo4jTransactionManager(Driver driver) {
        return new Neo4jTransactionManager(driver);
    }

    /**
     * Définit le gestionnaire de transactions principal pour JPA.
     *
     * @param entityManagerFactory l'usine d'EntityManager utilisée pour gérer les entités JPA
     * @return une instance de JpaTransactionManager
     */
    @Bean
    @Primary
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}