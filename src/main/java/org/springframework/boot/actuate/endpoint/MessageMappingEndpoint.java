package org.springframework.boot.actuate.endpoint;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageMappingInfo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;

/**
 * {@link Endpoint} to expose WebSocket message mappings
 *
 * @author Sergi Almar
 */
@Endpoint(id = "websocketmappings")
public class MessageMappingEndpoint {

	private final ApplicationContext applicationContext;
	
	public MessageMappingEndpoint(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@ReadOperation
	public WebSocketMappings mappings() {
		SimpAnnotationMethodMessageHandler handler = applicationContext
				.getBean(SimpAnnotationMethodMessageHandler.class);

		Map<SimpMessageMappingInfo, HandlerMethod> messageHandlerMethods = 
				this.filterByAnnotation(handler.getHandlerMethods(), MessageMapping.class);
		Map<SimpMessageMappingInfo, HandlerMethod> subscribeHandlerMethods = 
				this.filterByAnnotation(handler.getHandlerMethods(), SubscribeMapping.class);

		WebSocketMappings mappings = new WebSocketMappings();
		mappings.setPrefixes(handler.getDestinationPrefixes());
		mappings.setMessageMappings(extractMapping(messageHandlerMethods));
		mappings.setSubscribeMappings(extractMapping(subscribeHandlerMethods));
		
		return mappings;
	}

	private List<HandlerMapping> extractMapping(
			Map<SimpMessageMappingInfo, HandlerMethod> handlerMethods) {
		
		List<HandlerMapping> mappings = new ArrayList<>();

		for (SimpMessageMappingInfo info : handlerMethods.keySet()) {
			HandlerMethod handlerMethod = handlerMethods.get(info);

			HandlerMapping mapping = new HandlerMapping();
			mapping.setPredicate(info.getDestinationConditions().toString());
			mapping.setBeanName(handlerMethod.getBean().toString());
			mapping.setHandler(handlerMethod.toString());
			
			mappings.add(mapping);
		}

		return mappings;
	}

	private Map<SimpMessageMappingInfo, HandlerMethod> filterByAnnotation(
			Map<SimpMessageMappingInfo, HandlerMethod> handlerMethods, Class<? extends Annotation> annotation) {
		
		Map<SimpMessageMappingInfo, HandlerMethod> filteredHandlerMethods = new LinkedHashMap<>();

		for (SimpMessageMappingInfo info : handlerMethods.keySet()) {
			HandlerMethod handlerMethod = handlerMethods.get(info);

			if (handlerMethod.getMethodAnnotation(annotation) != null) {
				filteredHandlerMethods.put(info, handlerMethod);
			}
		}

		return filteredHandlerMethods;
	}
	
	/**
	 * A description of an application's message mappings. Primarily intended for
	 * serialization to JSON.
	 */
	public static final class WebSocketMappings {

		private Collection<String> prefixes;
		
		private List<HandlerMapping> subscribeMappings;
		
		private List<HandlerMapping> messageMappings;
		

		public Collection<String> getPrefixes() {
			return prefixes;
		}

		public void setPrefixes(Collection<String> prefixes) {
			this.prefixes = prefixes;
		}

		public List<HandlerMapping> getSubscribeMappings() {
			return subscribeMappings;
		}

		public void setSubscribeMappings(List<HandlerMapping> subscribeMappings) {
			this.subscribeMappings = subscribeMappings;
		}

		public List<HandlerMapping> getMessageMappings() {
			return messageMappings;
		}

		public void setMessageMappings(List<HandlerMapping> messageMappings) {
			this.messageMappings = messageMappings;
		}
	}
	
	/**
	 * A description of a handler method mappings. Primarily intended for
	 * serialization to JSON.
	 */
	public static final class HandlerMapping {
		
		private String predicate;
		
		private String beanName;
		
		private String handler;

		public String getPredicate() {
			return predicate;
		}

		public void setPredicate(String predicate) {
			this.predicate = predicate;
		}

		public String getBeanName() {
			return beanName;
		}

		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}

		public String getHandler() {
			return handler;
		}

		public void setHandler(String handler) {
			this.handler = handler;
		}
	}
}
