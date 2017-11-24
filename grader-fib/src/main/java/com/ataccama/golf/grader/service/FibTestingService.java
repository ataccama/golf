package com.ataccama.golf.grader.service;

import org.springframework.stereotype.Component;

import com.ataccama.golf.grader.service.TestingException;
import com.ataccama.golf.grader.service.TestingService;

@Component
public class FibTestingService implements TestingService {

	private static final String FIB_800 = "69283081864224717136290077681328518273399124385204820718966040597691435587278383112277161967532530675374170857404743017623467220361778016172106855838975759985190398725";

	@Override
	public int test(String code, String output) throws TestingException {
		if (!FIB_800.equals(output.trim())) {
			throw new TestingException("The result is wrong. It should start with 69...");
		}
		return code.trim().replace("\r\n", "\n").length();
	}
}
