package com.ataccama.golf.gateway.messaging;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ataccama.golf.commons.Constants;

@Configuration
public class MessagingQueueConfig {
	@Bean
	public Queue processedQueue() {
		return new Queue(Constants.TOPIC_PROCESSED + ".gateway");
	}

	@Bean
	public Binding processedBinding() {
		TopicExchange processedExchange = new TopicExchange(Constants.TOPIC_PROCESSED);
		return BindingBuilder.bind(processedQueue()).to(processedExchange).with("#");
	}

	@Bean
	public Queue gradedQueue() {
		return new Queue(Constants.TOPIC_GRADED + ".gateway");
	}

	@Bean
	public Binding gradedBinding() {
		TopicExchange gradedExchange = new TopicExchange(Constants.TOPIC_GRADED);
		return BindingBuilder.bind(gradedQueue()).to(gradedExchange).with("#");
	}

	@Bean
	public Queue registerQueue() {
		return new AnonymousQueue(new AnonymousQueue.Base64UrlNamingStrategy(Constants.TOPIC_REGISTER + ".gateway."));
	}

	@Bean
	public Binding registerBinding() {
		FanoutExchange registerExchange = new FanoutExchange(Constants.TOPIC_REGISTER);
		return BindingBuilder.bind(registerQueue()).to(registerExchange);
	}
}
