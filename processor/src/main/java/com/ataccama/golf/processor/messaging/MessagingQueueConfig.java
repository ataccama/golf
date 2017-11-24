package com.ataccama.golf.processor.messaging;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ataccama.golf.commons.Constants;
import com.ataccama.golf.processor.LanguageConfig;
import com.ataccama.golf.processor.processing.ProcessingReadyEvent;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MessagingQueueConfig implements ApplicationListener<ProcessingReadyEvent> {
	@Autowired
	private LanguageConfig languageConfig;

	@Autowired
	private RabbitListenerEndpointRegistry registry;

	@Bean
	public Queue submittedQueue() {
		return new Queue(Constants.TOPIC_SUBMITTED + ".processor." + languageConfig.getId());
	}

	@Bean
	public Binding submittedBinding() {
		TopicExchange submittedExchange = new TopicExchange(Constants.TOPIC_SUBMITTED);
		return BindingBuilder.bind(submittedQueue()).to(submittedExchange).with("*." + languageConfig.getId());
	}

	@Bean
	public Queue discoverQueue() {
		return new AnonymousQueue(new AnonymousQueue.Base64UrlNamingStrategy(Constants.TOPIC_DISCOVER + ".processor."));
	}

	@Bean
	public Binding discoverBinding() {
		FanoutExchange discoverExchange = new FanoutExchange(Constants.TOPIC_DISCOVER);
		return BindingBuilder.bind(discoverQueue()).to(discoverExchange);
	}

	public void delayedStart() {
		log.info("Starting listener containers.");
		for (MessageListenerContainer messageListenerContainer : registry.getListenerContainers()) {
			messageListenerContainer.start();
		}
	}

	@Override
	public void onApplicationEvent(ProcessingReadyEvent event) {
		delayedStart();
	}
}
