package org.springframework.boot.actuate.trace.websocket;

import java.util.List;

import org.springframework.boot.actuate.trace.http.HttpTrace;

/**
 * A repository for {@link WebSocketTrace}s.
 *
 * @author Sergi Almar
 */
public interface WebSocketTraceRepository {
	/**
	 * Find all {@link HttpTrace} objects contained in the repository.
	 * @return the results
	 */
	List<WebSocketTrace> findAll();

	/**
	 * Adds a trace to the repository.
	 * @param trace the trace to add
	 */
	void add(WebSocketTrace trace);
}