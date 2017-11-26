package com.ataccama.golf.gateway.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.Constants;
import com.ataccama.golf.commons.SolutionSubmitMessage;

@Component
public class MessagingServiceImpl implements MessagingService {
	@Autowired
	private RabbitTemplate template;

	@Override
	public void submit(SolutionSubmitMessage message) {
		String routingKey = message.getTask() + "." + message.getLanguage();
		template.convertAndSend(Constants.TOPIC_SUBMITTED, routingKey, message);
	}
}
