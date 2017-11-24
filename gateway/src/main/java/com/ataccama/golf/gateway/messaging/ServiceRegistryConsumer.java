package com.ataccama.golf.gateway.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.Constants;
import com.ataccama.golf.commons.RegisteredMessage;
import com.ataccama.golf.gateway.services.ServiceRegistry;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceRegistryConsumer {
	@Autowired
	private ServiceRegistry registry;

	@RabbitListener(queues = "#{registerQueue}")
	public void registered(RegisteredMessage message) {
		log.info("Updating list of registered services: {}", message);

		switch (message.getType()) {
		case Constants.GRADER:
			registry.registerTask(message.getService());
			break;
		case Constants.PROCESSOR:
			registry.registerLanguage(message.getService());
			break;
		default:
			log.error("Service of unknown type {}.", message.getType());
		}
	}
}