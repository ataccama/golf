package com.ataccama.golf.gateway.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.Constants;
import com.ataccama.golf.commons.DiscoverMessage;
import com.ataccama.golf.commons.SolutionSubmitMessage;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessagingServiceImpl implements MessagingService, ApplicationListener<ApplicationReadyEvent> {
	@Autowired
	private RabbitTemplate template;

	@Override
	public void submit(SolutionSubmitMessage message) {
		String routingKey = message.getTask() + "." + message.getLanguage();
		template.convertAndSend(Constants.TOPIC_SUBMITTED, routingKey, message);
	}

	@Override
	public void discover() {
		DiscoverMessage message = new DiscoverMessage();
		log.info("Sending discover message {}", message);
		template.convertAndSend(Constants.TOPIC_DISCOVER, null, message);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("Ready for discover");
		discover();
	}
}
