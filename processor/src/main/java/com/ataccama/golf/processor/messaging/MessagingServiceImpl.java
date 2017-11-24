package com.ataccama.golf.processor.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.Constants;
import com.ataccama.golf.commons.RegisteredMessage;
import com.ataccama.golf.commons.SolutionProcessedMessage;
import com.ataccama.golf.processor.LanguageConfig;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessagingServiceImpl implements MessagingService, ApplicationListener<ApplicationReadyEvent> {
	@Autowired
	private RabbitTemplate template;

	@Autowired
	private LanguageConfig languageConfig;

	@Override
	public void processed(SolutionProcessedMessage message) {
		String routingKey = message.getTask() + "." + message.getProcessingResult().name();
		log.info("Processed: routing " + routingKey + " for " + message);
		template.convertAndSend(Constants.TOPIC_PROCESSED, routingKey, message);
	}

	@Override
	public void register() {
		RegisteredMessage message = new RegisteredMessage(Constants.PROCESSOR, languageConfig.toService());
		log.info("Registering service: {}", message);
		template.convertAndSend(Constants.TOPIC_REGISTER, null, message);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("Ready for registring");
		register();
	}
}
