package com.ataccama.golf.gateway.services;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.Service;

@Component
public class ServiceRegistryImpl implements ServiceRegistry {
	private Set<Service> languages = new CopyOnWriteArraySet<>();
	private Set<Service> tasks = new CopyOnWriteArraySet<>();

	@Override
	public void registerLanguage(Service language) {
		languages.add(language);
	}

	@Override
	public void registerTask(Service task) {
		tasks.add(task);
	}

	@Override
	public Set<Service> getLanguages() {
		return Collections.unmodifiableSet(languages);
	}

	@Override
	public Set<Service> getTasks() {
		return Collections.unmodifiableSet(tasks);
	}

	@Override
	public boolean containsLanguage(String language) {
		return languages.stream().anyMatch(t -> t.getId().equals(language));
	}

	@Override
	public boolean containsTask(String task) {
		return tasks.stream().anyMatch(t -> t.getId().equals(task));
	}
}
