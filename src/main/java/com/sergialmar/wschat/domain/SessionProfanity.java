package com.sergialmar.wschat.domain;

import java.util.concurrent.atomic.AtomicLong;

import com.sergialmar.wschat.exception.TooMuchProfanityException;

/**
 * 
 * @author Sergi Almar
 */
public class SessionProfanity {

	private long maxProfanityLevel = Long.MAX_VALUE;
	
	private AtomicLong profanityLevel = new AtomicLong();
	
	public SessionProfanity() {}
	
	public SessionProfanity(int maxProfanityLevel) {
		this.maxProfanityLevel = maxProfanityLevel;
	}
	
	public void increment(long partialProfanity) {
		if(profanityLevel.intValue() + partialProfanity >= maxProfanityLevel) {
			profanityLevel.set(maxProfanityLevel);
			throw new TooMuchProfanityException("You reached the max profanity level. You are banned");
		}
		
		profanityLevel.addAndGet(partialProfanity);
	}
}
