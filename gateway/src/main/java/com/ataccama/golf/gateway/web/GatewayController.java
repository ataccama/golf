package com.ataccama.golf.gateway.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ataccama.golf.commons.Constants;
import com.ataccama.golf.commons.Service;
import com.ataccama.golf.gateway.SolutionMapper;
import com.ataccama.golf.gateway.db.DatabaseService;
import com.ataccama.golf.gateway.db.Solution;
import com.ataccama.golf.gateway.messaging.MessagingService;
import com.ataccama.golf.gateway.services.ServiceRegistry;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GatewayController {

	@Autowired
	private DatabaseService dbService;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private SolutionMapper mapper;

	@Autowired
	private ServiceRegistry registry;

	@PostMapping(path = "/submit")
	public UUID submit(@RequestBody NewSolution dto) {
		validateNewSolution(dto);

		Solution solution = dbService.save(mapper.toDb(dto));

		log.info("A new solution has been submitted: {} with id {}", dto, solution.getId());

		messagingService.submit(mapper.toMessage(solution));

		return solution.getId();
	}

	private void validateNewSolution(NewSolution dto) {
		if (dto.getEmail() == null || !dto.getEmail().matches("[a-zA-Z.+]+@[a-zA-Z.+]+")) {
			throw new BadRequestException("Invalid email.");
		}

		if (dto.getCode() == null) {
			throw new BadRequestException("The code is not specified.");
		}
		if (dto.getCode().length() > Constants.MAX_CODE_SIZE) {
			throw new BadRequestException("The source code is too big.");
		}

		if (dto.getLanguage() == null) {
			throw new BadRequestException("The language is not specified.");
		}
		if (!registry.containsLanguage(dto.getLanguage())) {
			throw new BadRequestException("The language is not supported.");
		}

		if (dto.getTask() == null) {
			throw new BadRequestException("The task is not specified.");
		}
		if (!registry.containsLanguage(dto.getLanguage())) {
			throw new BadRequestException("The task is not supported.");
		}
	}

	@GetMapping(path = "/check")
	public List<SolutionDTO> get(@RequestParam("ids") Set<UUID> ids) {
		List<Solution> solutions = dbService.get(ids);
		return mapper.toDtos(solutions);
	}

	@GetMapping(path = "/services")
	public Map<String, Collection<Service>> getServices() {
		Map<String, Collection<Service>> map = new HashMap<>();
		map.put("languages", registry.getLanguages());
		map.put("tasks", registry.getTasks());
		return map;
	}
}
