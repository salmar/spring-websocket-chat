package com.sergialmar.wschat.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.sergialmar.wschat.domain.SessionProfanity;
import com.sergialmar.wschat.event.ParticipantRepository;
import com.sergialmar.wschat.event.PresenceEventListener;
import com.sergialmar.wschat.util.ProfanityChecker;

@Configuration
public class ChatConfig {
	
	public static class Destinations {
		private Destinations() {}
		
		private static final String LOGIN  = "/topic/chat/login";
		private static final String LOGOUT = "/topic/chat/logout";
	}
	
	private static final int MAX_PROFANITY_LEVEL = 5;
	
	@Bean
	public ApplicationEventMulticaster applicationEventMulticaster() {
		SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
		multicaster.setTaskExecutor(Executors.newFixedThreadPool(10));
		return multicaster;
	}
	
	@Bean
	public PresenceEventListener presenceEventListener(SimpMessagingTemplate messagingTemplate) {
		PresenceEventListener presence = new PresenceEventListener(messagingTemplate, participantRepository());
		presence.setLoginDestination(Destinations.LOGIN);
		presence.setLogoutDestination(Destinations.LOGOUT);
		return presence;
	}
	
	@Bean 
	public ParticipantRepository participantRepository() {
		return new ParticipantRepository();
	}
	
	@Bean
	@Scope(value = "websocket", proxyMode =ScopedProxyMode.TARGET_CLASS)
	public SessionProfanity sessionProfanity() {
		return new SessionProfanity(MAX_PROFANITY_LEVEL);
	}
	
	@Bean
	public ProfanityChecker profanityFilter() {
		Set<String> profanities = new HashSet<>(Arrays.asList("damn", "crap", "ass"));
		ProfanityChecker checker = new ProfanityChecker();
		checker.setProfanities(profanities);
		return checker;
	}
}
