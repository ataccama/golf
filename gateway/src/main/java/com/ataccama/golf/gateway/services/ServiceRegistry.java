package com.ataccama.golf.gateway.services;

import java.util.Set;

import com.ataccama.golf.commons.Service;

public interface ServiceRegistry {
	void registerLanguage(Service language);

	void registerTask(Service task);

	Set<Service> getLanguages();

	Set<Service> getTasks();

	boolean containsLanguage(String language);

	boolean containsTask(String task);
}
