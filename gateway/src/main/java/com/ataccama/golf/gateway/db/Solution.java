package com.ataccama.golf.gateway.db;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.ataccama.golf.commons.Constants;
import com.ataccama.golf.commons.SolutionResult;

import lombok.Data;

@Data
@Entity
@EntityListeners(SolutionListener.class)
public class Solution {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(length = 16)
	@Access(AccessType.PROPERTY)
	private UUID id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String task;

	@Column(nullable = false, length = Constants.MAX_CODE_SIZE)
	private String code;

	@Column(nullable = false)
	private String language;

	@Column(nullable = false)
	private Instant created;

	@Column(nullable = false)
	private Instant updated;

	private Instant processingStarted;
	private Instant processingEnded;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SolutionResult processingResult = SolutionResult.UNKNOWN;

	@Column(length = Constants.MAX_CODE_SIZE)
	private String processingMessage;

	private Instant gradingStarted;
	private Instant gradingEnded;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SolutionResult gradingResult = SolutionResult.UNKNOWN;

	private Integer gradingScore;

	@Column(length = Constants.MAX_CODE_SIZE)
	private String gradingMessage;
}
