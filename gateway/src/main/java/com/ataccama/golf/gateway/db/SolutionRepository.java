package com.ataccama.golf.gateway.db;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface SolutionRepository extends CrudRepository<Solution, UUID> {
	// empty
}
