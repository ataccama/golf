package com.ataccama.golf.gateway.messaging;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MessagingConfig {
	@Value("${rabbit.host}")
	private String host;

	@Value("${rabbit.port}")
	private int port;

	@Value("${rabbit.username}")
	private String username;

	@Value("${rabbit.password}")
	private String password;

	@Bean
	public ConnectionFactory amqpConnectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host, port);
		cachingConnectionFactory.setUsername(username);
		cachingConnectionFactory.setPassword(password);
		return cachingConnectionFactory;
	}

	@Bean
	public MessageConverter jsonConverter(ObjectMapper mapper) {
		return new Jackson2JsonMessageConverter(mapper);
	}
}
