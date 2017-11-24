package com.ataccama.golf.grader.service;

public interface TestingService {
	int test(String code, String output) throws TestingException;
}
