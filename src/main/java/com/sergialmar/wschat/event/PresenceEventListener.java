package com.sergialmar.wschat.event;

import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Listener to track user presence. 
 * Sends notifications to the login destination when a connected event is received
 * and notifications to the logout destination when a disconnect event is received
 * 
 * @author Sergi Almar
 */
public class PresenceEventListener {
	
	private ParticipantRepository participantRepository;
	
	private SimpMessagingTemplate messagingTemplate;
	
	private String loginDestination;
	
	private String logoutDestination;
	
	public PresenceEventListener(SimpMessagingTemplate messagingTemplate, ParticipantRepository participantRepository) {
		this.messagingTemplate = messagingTemplate;
		this.participantRepository = participantRepository;
	}
		
	@EventListener
	private void handleSessionConnected(SessionConnectEvent event) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		String username = headers.getUser().getName();

		LoginEvent loginEvent = new LoginEvent(username);
		messagingTemplate.convertAndSend(loginDestination, loginEvent);
		
		// We store the session as we need to be idempotent in the disconnect event processing
		participantRepository.add(headers.getSessionId(), loginEvent);
	}
	
	@EventListener
	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		
		Optional.ofNullable(participantRepository.getParticipant(event.getSessionId()))
				.ifPresent(login -> {
					messagingTemplate.convertAndSend(logoutDestination, new LogoutEvent(login.getUsername()));
					participantRepository.removeParticipant(event.getSessionId());
				});
	}

	public void setLoginDestination(String loginDestination) {
		this.loginDestination = loginDestination;
	}

	public void setLogoutDestination(String logoutDestination) {
		this.logoutDestination = logoutDestination;
	}
}
