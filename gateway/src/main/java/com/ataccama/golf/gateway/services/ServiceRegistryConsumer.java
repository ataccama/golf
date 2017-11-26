package com.ataccama.golf.gateway.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.RegisteredMessage;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceRegistryConsumer {
	@Autowired
	private ServiceRegistry registry;

	@RabbitListener(queues = "#{registerQueue}")
	public void registered(RegisteredMessage message) {
		log.info("Updating list of registered services: {}", message);
		registry.register(message.getService());
	}
}