package org.springframework.boot.actuate.trace.websocket;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;

/**
 * {@link ChannelInterceptor} that logs messages to a {@link WebSocketTraceRepository}.
 *
 * @author Sergi Almar
 */
public class WebSocketTraceChannelInterceptor extends ChannelInterceptorAdapter {

	private final WebSocketTraceRepository traceRepository;

	
	public WebSocketTraceChannelInterceptor(WebSocketTraceRepository traceRepository) {
		this.traceRepository = traceRepository;
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		
		// Don't trace non-STOMP messages (like heartbeats)
		if(headerAccessor.getCommand() == null) {
			return;
		}
		
		WebSocketTrace trace = new WebSocketTrace();
		
		trace.setSessionId(headerAccessor.getSessionId());
		trace.setStompCommand(headerAccessor.getCommand().name());
		trace.setNativeHeaders(getNativeHeaders(headerAccessor));

		String payload = new String((byte[]) message.getPayload());
		
		if(!payload.isEmpty()) {
			trace.setPayload(payload);
		}
		
		traceRepository.add(trace);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getNativeHeaders(StompHeaderAccessor headerAccessor) {
		Map<String, List<String>> nativeHeaders = 
				(Map<String, List<String>>) headerAccessor.getHeader(NativeMessageHeaderAccessor.NATIVE_HEADERS);
		
		if(nativeHeaders == null) {
			return Collections.EMPTY_MAP;
		}
		
		Map<String, Object> traceHeaders = new LinkedHashMap<String, Object>();
		
		for(String header : nativeHeaders.keySet()) {
			List<String> headerValue = (List<String>) nativeHeaders.get(header);
			Object value = headerValue;
				
			if(headerValue.size() == 1) {
				value = headerValue.get(0);
			} else if(headerValue.isEmpty()) {
				value = "";
			}
			
			traceHeaders.put(header, value);
		}
		
		return traceHeaders;
	}
}
