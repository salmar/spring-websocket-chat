package com.sergialmar.wschat.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class ProfanityCheckerTests {

	private static ProfanityChecker profanityChecker;
	
	@BeforeAll
	public static void setup() {
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
