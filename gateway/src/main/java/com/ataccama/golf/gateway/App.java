package com.ataccama.golf.gateway;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableRabbit
@EnableTransactionManagement
public class App {

	public static void main(String[] args) {
		new SpringApplicationBuilder(App.class).run(args);
	}
}
