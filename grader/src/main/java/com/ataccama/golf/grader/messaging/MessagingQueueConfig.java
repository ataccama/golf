package com.ataccama.golf.grader.messaging;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ataccama.golf.commons.Constants;
import com.ataccama.golf.commons.SolutionResult;
import com.ataccama.golf.grader.TaskConfig;

@Configuration
public class MessagingQueueConfig {
	@Autowired
	private TaskConfig taskConfig;

	@Bean
	public Queue processedQueue() {
		return new Queue(Constants.TOPIC_PROCESSED + ".grader." + taskConfig.getId());
	}

	@Bean
	public Binding processedBinding() {
		TopicExchange processedExchange = new TopicExchange(Constants.TOPIC_PROCESSED);
		return BindingBuilder.bind(processedQueue()).to(processedExchange)
				.with(taskConfig.getId() + "." + SolutionResult.OK.name());
	}

	@Bean
	public Queue discoverQueue() {
		return new AnonymousQueue(new AnonymousQueue.Base64UrlNamingStrategy(Constants.TOPIC_DISCOVER + ".grader."));
	}

	@Bean
	public Binding discoverBinding() {
		FanoutExchange discoverExchange = new FanoutExchange(Constants.TOPIC_DISCOVER);
		return BindingBuilder.bind(discoverQueue()).to(discoverExchange);
	}
}
