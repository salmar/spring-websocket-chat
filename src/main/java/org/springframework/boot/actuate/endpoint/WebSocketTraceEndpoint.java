package org.springframework.boot.actuate.endpoint;

import java.util.List;

import org.springframework.boot.actuate.trace.Trace;
import org.springframework.boot.actuate.trace.TraceRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

@ConfigurationProperties(prefix = "endpoints.websockettrace", ignoreUnknownFields = false)
public class WebSocketTraceEndpoint extends AbstractEndpoint<List<Trace>> {

	private final TraceRepository repository;

	public WebSocketTraceEndpoint(TraceRepository repository) {
		super("websockettrace");
		Assert.notNull(repository, "Repository must not be null");
		this.repository = repository;
	}

	@Override
	public List<Trace> invoke() {
		return this.repository.findAll();
	}
}