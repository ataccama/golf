package com.ataccama.golf.grader;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableRabbit
public class App {

	public static void main(String[] args) {
		new SpringApplicationBuilder(App.class).run(args);
	}
}
