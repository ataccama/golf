package com.ataccama.golf.grader.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class PrimeTestingService implements TestingService {

	private final String huge;

	public PrimeTestingService() throws IOException {
		huge = new String(Files.readAllBytes(Paths.get("/huge.txt")), StandardCharsets.UTF_8);
	}

	@Override
	public int test(String code, String output) throws TestingException {
		if (huge.startsWith(output)) {
			return output.length();
		}
		if (output.length() > huge.length() && output.startsWith(huge)) {
			throw new TestingException("You are better than I assumed. Your output is longer than " + huge.length() + "characters.");
		}
		throw new TestingException("The result is incorrect.");
	}
}
