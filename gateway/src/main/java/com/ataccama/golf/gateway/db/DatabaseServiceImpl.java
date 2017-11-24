package com.ataccama.golf.gateway.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {

	@Autowired
	private SolutionRepository repository;

	@Override
	public Solution save(Solution solution) {
		repository.save(solution);
		return solution;
	}

	@Override
	public void update(UUID id, Consumer<Solution> updater) {
		Solution solution = repository.findOne(id);
		if (solution == null) {
			log.error("Couldn't find solution {} during update.", solution);
			return;
		}

		updater.accept(solution);
	}

	@Override
	public List<Solution> get(Set<UUID> ids) {
		List<Solution> results = new ArrayList<>();
		repository.findAll(ids).forEach(s -> {
			if (s != null)
				results.add(s);
		});
		return results;
	}
}
