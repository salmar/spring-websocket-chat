package com.sergialmar.wschat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configure(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .destinationMatchers("/user/queue/errors").permitAll()
                .anyMessage().hasRole("USER");
    }

    // avoid processing outbound channel
    public void configureClientOutboundChannel(ChannelRegistration registration) {}
}