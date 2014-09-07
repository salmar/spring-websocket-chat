package com.sergialmar.wschat.event;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Listener to track user presence. 
 * Sends notifications to the login destination when a connected event is received
 * and notifications to the logout destination when a disconnect event is received
 * 
 * @author Sergi Almar
 */
public class PresenceEventListener implements ApplicationListener<ApplicationEvent> {
	
	private ParticipantRepository participantRepository;
	private SimpMessagingTemplate messagingTemplate;
	private String loginDestination;
	private String logoutDestination;
	
	public PresenceEventListener(SimpMessagingTemplate messagingTemplate, ParticipantRepository participantRepository) {
		this.messagingTemplate = messagingTemplate;
		this.participantRepository = participantRepository;
	}
	
	public void onApplicationEvent(ApplicationEvent event) {
		
		if(event instanceof SessionConnectEvent) {
			handleSessionConnected((SessionConnectEvent) event);
		} else if(event instanceof SessionDisconnectEvent) {
			handleSessionDisconnect((SessionDisconnectEvent) event);
		}
	}
	
	private void handleSessionConnected(SessionConnectEvent event) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		String username = headers.getUser().getName();

		LoginEvent loginEvent = new LoginEvent(username);
		messagingTemplate.convertAndSend(loginDestination, loginEvent);
		
		// We store the session as we need to be idempotent in the disconnect event processing
		participantRepository.add(headers.getSessionId(), loginEvent);
	}
	
	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		
		Optional.ofNullable(participantRepository.getParticipant(event.getSessionId()))
				.ifPresent(login -> {
					messagingTemplate.convertAndSend(logoutDestination, new LogoutEvent(login.getUsername()));
					participantRepository.removeParticipant(event.getSessionId());
				});
	}


	public ParticipantRepository getParticipantRepository() {
		return participantRepository;
	}

	public void setParticipantRepository(ParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}

	public SimpMessagingTemplate getMessagingTemplate() {
		return messagingTemplate;
	}

	public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	public String getLoginDestination() {
		return loginDestination;
	}

	public void setLoginDestination(String loginDestination) {
		this.loginDestination = loginDestination;
	}

	public String getLogoutDestination() {
		return logoutDestination;
	}

	public void setLogoutDestination(String logoutDestination) {
		this.logoutDestination = logoutDestination;
	}
}
