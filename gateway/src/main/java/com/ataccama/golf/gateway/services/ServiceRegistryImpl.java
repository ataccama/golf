package com.ataccama.golf.gateway.services;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.Service;
import com.ataccama.golf.commons.ServiceType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceRegistryImpl implements ServiceRegistry {
	private Map<Service, Instant> services = new HashMap<>();

	@Override
	public void register(Service service) {
		services.put(service, Instant.now());

		removeOld();
	}

	@Override
	public List<Service> getServices(ServiceType type) {
		synchronized (services) {
			return services.keySet().stream().filter(s -> type.equals(s.getType()))
					.sorted(Comparator.comparing(Service::getOrder)).collect(Collectors.toList());
		}
	}

	@Override
	public boolean contains(ServiceType type, String id) {
		synchronized (services) {
			return services.keySet().stream().anyMatch(s -> type.equals(s.getType()) && id.equals(s.getId()));
		}
	}

	private void removeOld() {
		Instant now = Instant.now();
		synchronized (services) {
			for (Iterator<Entry<Service, Instant>> iterator = services.entrySet().iterator(); iterator.hasNext();) {
				Entry<Service, Instant> entry = iterator.next();
				if (entry.getValue().plusMillis(DiscoveryConstants.REGISTRY_RETENTION).isBefore(now)) {
					log.info(MessageFormat.format("Removing service {0} which was last registered {1}", entry.getKey(),
							entry.getValue()));
					iterator.remove();
				}
			}
		}
	}
}
