package com.ataccama.golf.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredMessage {
	private String type;
	private Service service;
}
