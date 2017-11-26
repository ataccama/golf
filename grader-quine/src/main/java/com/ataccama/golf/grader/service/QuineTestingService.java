package com.ataccama.golf.grader.service;

import org.springframework.stereotype.Component;

import com.ataccama.golf.grader.service.TestingException;
import com.ataccama.golf.grader.service.TestingService;

@Component
public class QuineTestingService implements TestingService {

	@Override
	public int test(String code, String output) throws TestingException {
		code = code.trim().replace("\r\n", "\n"); // NOSONAR
		output = output.trim().replace("\r\n", "\n"); // NOSONAR

		if (code.isEmpty()) {
			throw new TestingException("Nice try, but we don not accept empty programs.");
		}

		if (!code.equals(output)) {
			throw new TestingException("The output must be the same as the program itself.");
		}
		return code.replace("\r\n", "\n").length();
	}
}
