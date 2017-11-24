package com.ataccama.golf.processor;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableRabbit
@EnableAsync
public class App {

	public static void main(String[] args) {
		new SpringApplicationBuilder(App.class).run(args);
	}
}
