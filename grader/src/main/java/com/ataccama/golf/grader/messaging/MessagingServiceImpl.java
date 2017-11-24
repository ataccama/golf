package com.ataccama.golf.grader.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.Constants;
import com.ataccama.golf.commons.RegisteredMessage;
import com.ataccama.golf.commons.SolutionGradedMessage;
import com.ataccama.golf.grader.TaskConfig;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessagingServiceImpl implements MessagingService, ApplicationListener<ApplicationReadyEvent> {
	@Autowired
	private RabbitTemplate template;

	@Autowired
	private TaskConfig taskConfig;

	@Override
	public void graded(SolutionGradedMessage message) {
		String routingKey = message.getTask() + "." + message.getGradingResult().name();
		log.info("Graded: routing " + routingKey + " for " + message);
		template.convertAndSend(Constants.TOPIC_GRADED, routingKey, message);
	}

	@Override
	public void register() {
		RegisteredMessage message = new RegisteredMessage(Constants.GRADER, taskConfig.toService());
		log.info("Registering service: {}", message);
		template.convertAndSend(Constants.TOPIC_REGISTER, null, message);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		log.info("Ready for registring");
		register();
	}
}
