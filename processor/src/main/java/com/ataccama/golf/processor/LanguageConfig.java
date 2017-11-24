package com.ataccama.golf.processor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.Service;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("language")
public class LanguageConfig {
	private String id;
	private String name;
	private String description;

	public Service toService() {
		return new Service(id, name, description);
	}
}
