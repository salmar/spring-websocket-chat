package org.springframework.boot.actuate.autoconfigure.websocket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.autoconfigure.trace.websocket.WebSocketTraceAutoconfiguration;
import org.springframework.boot.actuate.autoconfigure.trace.websocket.WebSocketTraceProperties;
import org.springframework.boot.actuate.endpoint.MessageMappingEndpoint;
import org.springframework.boot.actuate.endpoint.WebSocketStatsEndpoint;
import org.springframework.boot.actuate.trace.websocket.WebSocketTraceChannelInterceptor;
import org.springframework.boot.actuate.trace.websocket.WebSocketTraceEndpoint;
import org.springframework.boot.actuate.trace.websocket.WebSocketTraceRepository;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for WebSocket endpoints.
 *
 * @author Sergi Almar
 */
@Configuration
@AutoConfigureAfter(WebSocketTraceAutoconfiguration.class)
public class WebSocketEndpointAutoConfiguration {

	@Bean
	@ConditionalOnBean(WebSocketTraceRepository.class)
	@ConditionalOnMissingBean
	@ConditionalOnEnabledEndpoint
	@Description("Spring Actuator endpoint to expose WebSocket traces")
	public WebSocketTraceEndpoint websocketTraceEndpoint(WebSocketTraceRepository webSocketTraceRepository) {
		return new WebSocketTraceEndpoint(webSocketTraceRepository);
	}
	
	@Bean
	@ConditionalOnBean(WebSocketMessageBrokerStats.class)
	@ConditionalOnEnabledEndpoint
	@Description("Spring Actuator endpoint to expose WebSocket stats")
	public WebSocketStatsEndpoint websocketEndpoint(WebSocketMessageBrokerStats stats) {
		return new WebSocketStatsEndpoint(stats);
	}
	
	@Bean
	@ConditionalOnEnabledEndpoint
	@Description("Spring Actuator endpoint to expose WebSocket message mappings")
	public MessageMappingEndpoint messageMappingEndpoint(ApplicationContext context) {
		return new MessageMappingEndpoint(context);
	}

	@Bean
	@ConditionalOnBean(WebSocketTraceRepository.class)
	@Description("Channel interceptor for WebSocket tracing")
	public WebSocketTraceChannelInterceptor webSocketTraceChannelInterceptor(
			WebSocketTraceRepository webSocketTraceRepository) {
		return new WebSocketTraceChannelInterceptor(webSocketTraceRepository);
	}

	@Bean
	@ConditionalOnBean(name = { "clientInboundChannel", "clientOutboundChannel" })
	@Description("CLR that adds the required channel interceptors for tracing")
	public CommandLineRunner addTraceInterceptor(WebSocketTraceProperties webSocketTraceProperties, 
			WebSocketTraceChannelInterceptor webSocketTraceChannelInterceptor,
			ExecutorSubscribableChannel clientInboundChannel, 
			ExecutorSubscribableChannel clientOutboundChannel) {

		return (args) -> {

			if (webSocketTraceProperties.isTraceInbound()) {
				clientInboundChannel.addInterceptor(webSocketTraceChannelInterceptor);
			}

			if (webSocketTraceProperties.isTraceOutbound()) {
				clientOutboundChannel.addInterceptor(webSocketTraceChannelInterceptor);
			}
		};
	}
	
	
}