package com.sergialmar.wschat.util;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ProfanityCheckerTests {

	private ProfanityChecker profanityChecker;
	
	@Before
	public void setup() {
		Set<String> profanities = new HashSet<>();
		profanities.add(".NET");
		profanities.add("Micro$oft");
		
		profanityChecker = new ProfanityChecker();
		profanityChecker.setProfanities(profanities);
	}
	
	@Test
	public void filterProfanities() {
		String filtered = profanityChecker.filter("Hi! I'm a .NET developer");
		assertEquals(filtered, "Hi! I'm a developer");
	}
	
	@Test
	public void countProfanities() {
		long profanityLevel = profanityChecker.getMessageProfanity("Hi! I'm a .NET developer working at Micro$oft");
		
		assertEquals(profanityLevel, 2);
	}
}
