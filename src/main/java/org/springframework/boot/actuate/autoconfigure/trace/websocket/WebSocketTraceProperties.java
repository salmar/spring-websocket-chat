package org.springframework.boot.actuate.autoconfigure.trace.websocket;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for WebSocket tracing 
 *
 * @author Sergi Almar
 */
@ConfigurationProperties(prefix = "management.trace.websocket")
public class WebSocketTraceProperties {

	private boolean traceInbound = true;
	
	private boolean traceOutbound = false;
	
	private int capacity  = 100;
	
	private boolean reverse = true;

	
	public boolean isTraceInbound() {
		return traceInbound;
	}

	public void setTraceInbound(boolean traceInbound) {
		this.traceInbound = traceInbound;
	}

	public boolean isTraceOutbound() {
		return traceOutbound;
	}

	public void setTraceOutbound(boolean traceOutbound) {
		this.traceOutbound = traceOutbound;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
}
