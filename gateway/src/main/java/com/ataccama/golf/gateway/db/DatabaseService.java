package com.ataccama.golf.gateway.db;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public interface DatabaseService {
	List<Solution> get(Set<UUID> ids);

	Solution save(Solution solution);

	void update(UUID id, Consumer<Solution> updater);
}
