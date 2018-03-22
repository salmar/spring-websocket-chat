package org.springframework.boot.actuate.trace.websocket;

import java.util.List;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.util.Assert;

/**
 * {@link Endpoint} to expose WebSocket traces
 *
 * @author Sergi Almar
 */
@Endpoint(id = "websockettrace")
public class WebSocketTraceEndpoint {

	private final WebSocketTraceRepository repository;

	public WebSocketTraceEndpoint(WebSocketTraceRepository repository) {
		Assert.notNull(repository, "Repository must not be null");
		this.repository = repository;
	}
	
	@ReadOperation
	public WebSocketTraceDescriptor traces() {
		return new WebSocketTraceDescriptor(this.repository.findAll());
	}

	/**
	 * A description of an application's {@link WebSocketTraceWebSocketTrace} entries. Primarily intended for
	 * serialization to JSON.
	 */
	static final class WebSocketTraceDescriptor {

		private final List<WebSocketTrace> traces;

		private WebSocketTraceDescriptor(List<WebSocketTrace> traces) {
			this.traces = traces;
		}

		public List<WebSocketTrace> getTraces() {
			return this.traces;
		}
	}
}
