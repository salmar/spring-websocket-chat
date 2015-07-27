package com.sergialmar.wschat.actuator;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

/**
 * {@link Endpoint} to expose WebSocket stats
 *
 * @author Sergi Almar
 */
@ConfigurationProperties(prefix = "endpoints.websocket", ignoreUnknownFields = true)
public class WebSocketEndpoint extends AbstractEndpoint<WebSocketMessageBrokerStats> {

	private WebSocketMessageBrokerStats webSocketMessageBrokerStats;
	
	public WebSocketEndpoint(WebSocketMessageBrokerStats webSocketMessageBrokerStats) {
		super("websocket");
		this.webSocketMessageBrokerStats = webSocketMessageBrokerStats;
	}

	@Override
	public WebSocketMessageBrokerStats invoke() {
		return webSocketMessageBrokerStats;
	}
}
