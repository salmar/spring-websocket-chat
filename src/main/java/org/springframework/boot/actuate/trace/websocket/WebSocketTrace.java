package org.springframework.boot.actuate.trace.websocket;

import java.time.Instant;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * A repository for {@link WebSocketTrace}s.
 *
 * @author Sergi Almar
 */
public final class WebSocketTrace {

	private final Instant timestamp = Instant.now();

	private String sessionId;

	private String stompCommand;
	
	private Map<String, Object> nativeHeaders;
	
	private String payload;

	
	public Instant getTimestamp() {
		return timestamp;
	}

	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		if (StringUtils.hasText(sessionId)) {
			this.sessionId = sessionId;
		}
	}
	
	public String getStompCommand() {
		return stompCommand;
	}

	public void setStompCommand(String stompCommand) {
		this.stompCommand = stompCommand;
	}

	public Map<String, Object> getNativeHeaders() {
		return nativeHeaders;
	}

	public void setNativeHeaders(Map<String, Object> nativeHeaders) {
		this.nativeHeaders = nativeHeaders;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
}
