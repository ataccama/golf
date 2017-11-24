package com.ataccama.golf.grader.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.SolutionGradedMessage;
import com.ataccama.golf.commons.SolutionProcessedMessage;
import com.ataccama.golf.commons.SolutionResult;
import com.ataccama.golf.grader.messaging.MessagingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GradingServiceImpl implements GradingService {
	@Autowired
	private MessagingService messagingService;

	@Autowired
	private TestingService testingService;

	@Override
	public void grade(SolutionProcessedMessage message) {
		SolutionGradedMessage gradedMessage = new SolutionGradedMessage();
		gradedMessage.setId(message.getId());
		gradedMessage.setTask(message.getTask());
		gradedMessage.setGradingStarted(Instant.now());

		try {
			int score = testingService.test(message.getCode(), message.getOutput());
			gradedMessage.setGradingScore(score);
			gradedMessage.setGradingResult(SolutionResult.OK);
		} catch (TestingException e) {
			gradedMessage.setGradingResult(SolutionResult.FAILED);
			gradedMessage.setGradingMessage(e.getMessage());
		}

		gradedMessage.setGradingEnded(Instant.now());

		log.info("The solution was graded with state: {}", gradedMessage);
		messagingService.graded(gradedMessage);
	}
}
