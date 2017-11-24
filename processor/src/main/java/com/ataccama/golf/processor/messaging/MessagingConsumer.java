package com.ataccama.golf.processor.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.DiscoverMessage;
import com.ataccama.golf.commons.SolutionSubmitMessage;
import com.ataccama.golf.processor.processing.ProcessingService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessagingConsumer {
	@Autowired
	private ProcessingService processingService;

	@Autowired
	private MessagingService messagingService;

	@RabbitListener(queues = "#{submittedQueue}")
	public void submitted(SolutionSubmitMessage message) {
		log.info("Will process the following message: {}", message);
		processingService.process(message);
	}

	@RabbitListener(queues = "#{discoverQueue}")
	public void discover(DiscoverMessage message) {
		log.info("Discover request: {}", message);
		messagingService.register();
	}
}
