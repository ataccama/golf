package com.ataccama.golf.gateway.db;

import java.time.Instant;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SolutionListener {
	@PrePersist
	public void prePersist(Solution solution) {
		solution.setCreated(Instant.now());
		preUpdate(solution);
	}

	@PreUpdate
	public void preUpdate(Solution solution) {
		log.debug("Before update: " + solution);
		solution.setUpdated(Instant.now());
	}
}
