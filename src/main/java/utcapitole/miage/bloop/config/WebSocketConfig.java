package utcapitole.miage.bloop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration de WebSocket pour activer la messagerie en temps réel avec STOMP.
 * Cette classe configure les points de terminaison WebSocket et le broker de messages.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Enregistre les points de terminaison STOMP pour permettre aux clients de se connecter.
     * Le point de terminaison `/ws` est défini et SockJS est activé pour la compatibilité avec les anciens navigateurs.
     *
     * @param registry Le registre des points de terminaison STOMP.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Définit le point de terminaison STOMP.
                .setAllowedOriginPatterns("*") // Autorise toutes les origines (CORS).
                .withSockJS(); // Active SockJS pour la compatibilité.
    }

    /**
     * Configure le broker de messages pour gérer les destinations des messages.
     * - Active un broker simple pour les destinations commençant par `/queue`.
     * - Définit le préfixe des destinations d'application comme `/app`.
     * - Définit le préfixe des destinations utilisateur comme `/user`.
     *
     * @param config Le registre de configuration du broker de messages.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue", "/topic"); // Active un broker simple pour les files d'attente.
        config.setApplicationDestinationPrefixes("/app"); // Préfixe pour les messages destinés à l'application.
        config.setUserDestinationPrefix("/user"); // Préfixe pour les messages destinés à un utilisateur spécifique.
    }
}