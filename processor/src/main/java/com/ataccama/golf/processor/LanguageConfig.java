package com.ataccama.golf.processor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.Service;
import com.ataccama.golf.commons.ServiceType;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("language")
public class LanguageConfig {
	private int order;
	private String id;
	private String name;
	private String description;

	public Service toService() {
		return new Service(ServiceType.LANGUAGE, order, id, name, description);
	}
}
