package org.springframework.boot.actuate.autoconfigure.trace.websocket;

import org.springframework.boot.actuate.trace.websocket.InMemoryWebSocketTraceRepository;
import org.springframework.boot.actuate.trace.websocket.WebSocketTraceRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;


/**
 * {@link EnableAutoConfiguration Auto-configuration} for WebSocket tracing.
 *
 * @author Sergi Almar
 */
@Configuration
@ConditionalOnClass(WebSocketHandler.class)
@ConditionalOnProperty(prefix = "management.trace.websocket", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(WebSocketTraceProperties.class)
public class WebSocketTraceAutoconfiguration {
	
	@Bean
	@ConditionalOnMissingBean(WebSocketTraceRepository.class)
	public InMemoryWebSocketTraceRepository webSocketTraceRepository(WebSocketTraceProperties traceProperties) {
		InMemoryWebSocketTraceRepository traceRepository =  new InMemoryWebSocketTraceRepository();
		traceRepository.setCapacity(traceProperties.getCapacity());
		traceRepository.setReverse(traceProperties.isReverse());
		
		return traceRepository;
	}
}