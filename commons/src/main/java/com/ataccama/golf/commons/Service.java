package com.ataccama.golf.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {
	private ServiceType type;
	private int order;

	private String id;
	private String name;
	private String description;
}
