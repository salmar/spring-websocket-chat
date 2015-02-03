package com.sergialmar.wschat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
        		.antMatchers(SimpMessageType.MESSAGE, "/topic/chat.login", 
        											  "/topic/chat.logout", 
        											  "/topic/chat.message").denyAll()
                .anyMessage().authenticated();
    }
}