package com.ataccama.golf.gateway.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.Constants;
import com.ataccama.golf.commons.DiscoverMessage;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceDiscovery implements ApplicationListener<ApplicationReadyEvent> {
	@Autowired
	private RabbitTemplate template;

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

	@Scheduled(fixedRate = DiscoveryConstants.DISCOVERY_PERIOD, initialDelay = DiscoveryConstants.DISCOVERY_PERIOD)
	public void scheduledDiscover() {
		log.info("Periodic discover");
		discover();
	}
}
