package com.sample_project.migration_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // The frontend will listen to URLs starting with "/topic" to receive live map updates
        config.enableSimpleBroker("/topic");

        // The frontend will send GPS coordinates to URLs starting with "/app"
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // This is the main URL the frontend connects to.
        // We add your frontend localhosts here so CORS doesn't block the connection.
        registry.addEndpoint("/ws-location")
                .setAllowedOrigins("http://localhost:3000", "http://localhost:5173","https://migration-frontend-7erdi2mo0-prasadwani01-8281s-projects.vercel.app")
                .withSockJS(); // Fallback in case a browser doesn't support modern WebSockets
    }
}
