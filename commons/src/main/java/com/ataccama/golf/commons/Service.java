package com.ataccama.golf.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "type", "id" })
public class Service {
	private ServiceType type;
	private int order;

	private String id;
	private String name;
	private String description;
}
