package com.sergialmar.wschat.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Checks for profanities and filters them
 * 
 * @author Sergi Almar
 */
public class ProfanityChecker {

	private Set<String> profanities = new HashSet<>();

	public long getMessageProfanity(String message) {
		return Arrays.stream(message.split(" ")) //
				.filter(word -> profanities.contains(word)) //
				.count();
	}

	public String filter(String message) {
		return Arrays.stream(message.split(" "))//
				.filter(word -> !profanities.contains(word)) //
				.collect(Collectors.joining(" "));
	}

	public Set<String> getProfanities() {
		return profanities;
	}

	public void setProfanities(Set<String> profanities) {
		this.profanities = profanities;
	}
}
