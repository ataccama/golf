package com.ataccama.golf.gateway.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.SolutionGradedMessage;
import com.ataccama.golf.commons.SolutionProcessedMessage;
import com.ataccama.golf.gateway.SolutionMapper;
import com.ataccama.golf.gateway.db.DatabaseService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RabbitListener(queues = { "#{processedQueue}", "#{gradedQueue}" })
public class MessagingConsumer {
	@Autowired
	private DatabaseService dbService;

	@Autowired
	private SolutionMapper mapper;

	@RabbitHandler
	public void processed(SolutionProcessedMessage message) {
		log.info("Updating database record about processing: {}", message);
		dbService.update(message.getId(), solution -> mapper.intoDb(message, solution));
	}

	@RabbitHandler
	public void graded(SolutionGradedMessage message) {
		log.info("Updating database record about grading: {}", message);
		dbService.update(message.getId(), solution -> mapper.intoDb(message, solution));
	}
}
