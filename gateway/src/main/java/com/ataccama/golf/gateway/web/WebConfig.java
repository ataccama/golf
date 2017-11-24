package com.ataccama.golf.gateway.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class WebConfig {
	/**
	 * Configure JacksonMessageConverter to map java.Instant in ISO-8601 format
	 * ('2011-12-03T10:15:30Z')
	 */
	@Bean
	MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return new MappingJackson2HttpMessageConverter(objectMapper);
	}
}
