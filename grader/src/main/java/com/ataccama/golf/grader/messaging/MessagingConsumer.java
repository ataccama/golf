package com.ataccama.golf.grader.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.DiscoverMessage;
import com.ataccama.golf.commons.SolutionProcessedMessage;
import com.ataccama.golf.grader.service.GradingService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessagingConsumer {
	@Autowired
	private GradingService gradingService;

	@Autowired
	private MessagingService messagingService;

	@RabbitListener(queues = "#{processedQueue}")
	public void processed(SolutionProcessedMessage message) {
		log.info("Will grade the following message: {}", message);
		gradingService.grade(message);
	}

	@RabbitListener(queues = "#{discoverQueue}")
	public void discover(DiscoverMessage message) {
		log.info("Discover request: {}", message);
		messagingService.register();
	}
}
