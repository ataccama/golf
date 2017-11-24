package com.ataccama.golf.processor.processing;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.Instant;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ataccama.golf.commons.SolutionProcessedMessage;
import com.ataccama.golf.commons.SolutionResult;
import com.ataccama.golf.commons.SolutionSubmitMessage;
import com.ataccama.golf.processor.messaging.MessagingService;
import com.ataccama.golf.processor.processing.errors.RunningException;
import com.ataccama.golf.processor.processing.errors.RunningFailedException;
import com.ataccama.golf.processor.processing.errors.RunningInternalErrorException;
import com.ataccama.golf.processor.processing.errors.RunningTimeoutException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProcessingServiceImpl implements ProcessingService {
	private static final int MAX_LENGTH = 1 << 12; // 16 kB
	private static final String ROOT_DIR = "/solutions";

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private RunningService runningService;

	public ProcessingServiceImpl() {
		File root = new File(ROOT_DIR);
		if (!root.isDirectory()) {
			throw new IllegalStateException("The rootDir " + ROOT_DIR + " is not an existing directory");
		}
	}

	@Override
	public void process(SolutionSubmitMessage solution) {
		SolutionProcessedMessage processedMessage = new SolutionProcessedMessage();
		processedMessage.setId(solution.getId());
		processedMessage.setCode(solution.getCode());
		processedMessage.setTask(solution.getTask());

		File solutionDir = new File(ROOT_DIR, solution.getId().toString());
		try {
			File code = new File(solutionDir, "code");
			File stdout = new File(solutionDir, "stdout");

			try {
				FileUtils.writeStringToFile(code, solution.getCode(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				log.error(MessageFormat.format("Failed to save code into file {0}.", code.getAbsolutePath()), e);
				processedMessage.setProcessingResult(SolutionResult.INTERNAL_ERROR);
				processedMessage.setProcessingMessage("Failed to save code into file.");
				messagingService.processed(processedMessage);
				return;
			}

			try {
				run(processedMessage, solutionDir);
			} catch (RunningException e) {
				messagingService.processed(processedMessage);
				return;
			} catch (InterruptedException e) {
				messagingService.processed(processedMessage);
				Thread.currentThread().interrupt();
				return;
			}

			readOutput(processedMessage, stdout);
			messagingService.processed(processedMessage);
		} finally {
			FileUtils.deleteQuietly(solutionDir);
		}
	}

	private void run(SolutionProcessedMessage processedMessage, File solutionDir)
			throws RunningException, InterruptedException {
		processedMessage.setProcessingStarted(Instant.now());
		try {
			runningService.run(solutionDir);
			processedMessage.setProcessingResult(SolutionResult.OK);
		} catch (RunningTimeoutException e) {
			processedMessage.setProcessingResult(SolutionResult.TIMEOUT);
			processedMessage.setProcessingMessage(e.getMessage());
			throw e;
		} catch (RunningFailedException e) {
			processedMessage.setProcessingResult(SolutionResult.FAILED);
			processedMessage.setProcessingMessage(e.getMessage());
			throw e;
		} catch (RunningInternalErrorException | InterruptedException e) {
			processedMessage.setProcessingResult(SolutionResult.INTERNAL_ERROR);
			processedMessage.setProcessingMessage(e.getMessage());
			throw e;
		} finally {
			processedMessage.setProcessingEnded(Instant.now());
		}
	}

	private void readOutput(SolutionProcessedMessage processedMessage, File stdout) {
		if (!stdout.isFile()) {
			processedMessage.setProcessingResult(SolutionResult.FAILED);
			processedMessage.setProcessingMessage("The output file is not a file.");
		} else if (stdout.length() > MAX_LENGTH) {
			processedMessage.setProcessingResult(SolutionResult.FAILED);
			processedMessage.setProcessingMessage("The output file is too large: " + stdout.length());
		} else {
			try {
				String output = FileUtils.readFileToString(stdout, StandardCharsets.UTF_8);
				processedMessage.setOutput(output);
			} catch (IOException e) {
				log.error(MessageFormat.format("Failed to read output from file {0}.", stdout.getAbsolutePath()), e);
				processedMessage.setProcessingResult(SolutionResult.INTERNAL_ERROR);
				processedMessage.setProcessingMessage("Failed to read output from file");
			}
		}
	}
}
