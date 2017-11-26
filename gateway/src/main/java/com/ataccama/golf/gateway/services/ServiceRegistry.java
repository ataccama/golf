package com.ataccama.golf.gateway.services;

import java.util.List;

import com.ataccama.golf.commons.Service;
import com.ataccama.golf.commons.ServiceType;

public interface ServiceRegistry {
	void register(Service service);

	List<Service> getServices(ServiceType type);

	boolean contains(ServiceType type, String id);
}
