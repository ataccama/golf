package com.ataccama.golf.grader;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.Service;
import com.ataccama.golf.commons.ServiceType;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("task")
public class TaskConfig {
	private int order;
	private String id;
	private String name;
	private String description;

	public Service toService() {
		return new Service(ServiceType.TASK, order, id, name, description);
	}
}
