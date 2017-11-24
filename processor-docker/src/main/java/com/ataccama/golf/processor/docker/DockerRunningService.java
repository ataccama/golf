package com.ataccama.golf.processor.docker;

import java.io.File;
import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.ataccama.golf.processor.processing.ProcessingReadyEvent;
import com.ataccama.golf.processor.processing.RunningService;
import com.ataccama.golf.processor.processing.errors.RunningException;
import com.ataccama.golf.processor.processing.errors.RunningFailedException;
import com.ataccama.golf.processor.processing.errors.RunningInternalErrorException;
import com.ataccama.golf.processor.processing.errors.RunningTimeoutException;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerClient.LogsParam;
import com.spotify.docker.client.DockerClient.RemoveContainerParam;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.ContainerNotFoundException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.HostConfig.Bind;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DockerRunningService implements RunningService, ApplicationListener<ApplicationReadyEvent> {
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Value("${docker.image}")
	private String dockerImage;

	@Value("${docker.timeout}")
	private int timeout;

	private DockerClient docker;

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		AtomicReference<ScheduledFuture<?>> waitForDocker = new AtomicReference<>();
		waitForDocker.set(executorService.scheduleAtFixedRate(() -> {
			try {
				docker = DefaultDockerClient.fromEnv().build();
				log.info("Docker is ready in version {}.", docker.version().version());

				docker.pull(dockerImage);

				applicationEventPublisher.publishEvent(new ProcessingReadyEvent(this));

				// cancel the schedule
				ScheduledFuture<?> scheduledFuture = waitForDocker.get();
				if (scheduledFuture != null) {
					scheduledFuture.cancel(false);
				}
			} catch (InterruptedException e) {
				log.info("Interrupted while pulling docker image " + dockerImage);
				Thread.currentThread().interrupt();
			} catch (Exception e) {
				log.info("Waiting for docker with exception.", e);
				// try again later
			}
		}, 0, 500, TimeUnit.MILLISECONDS));
	}

	@Override
	public void run(File solutionDir) throws RunningException, InterruptedException {
		if (docker == null) {
			log.warn("No processing is possible until docker is ready.");
			return;
		}

		String containerId = createContainer(solutionDir);

		ScheduledFuture<?> s1 = null;
		ScheduledFuture<?> s2 = null;
		AtomicInteger statusCode = new AtomicInteger(-1);
		try {
			startContainer(containerId);
			CountDownLatch latch = new CountDownLatch(1);

			s1 = executorService.schedule(() -> {
				log.debug("Timeouting container " + containerId);
				latch.countDown();
			}, timeout, TimeUnit.MILLISECONDS);
			s2 = executorService.schedule(() -> {
				statusCode.set(waitForContainer(containerId));
				log.debug("Finished waiting for container " + containerId);
				latch.countDown();
			}, 0, TimeUnit.MILLISECONDS);

			log.debug("Waiting for either timeout or exit.");
			latch.await();
			s1.cancel(true);
			s2.cancel(true);

			int status = statusCode.get();
			log.debug("Ended with status " + status);
			if (status < 0) {
				throw new RunningTimeoutException(timeout);
			} else if (status > 0) {
				reportFailure(containerId, status);
			}
		} finally {
			removeContainer(containerId);
		}
	}

	private String createContainer(File solutionDir) throws RunningInternalErrorException, InterruptedException {
		log.debug("Creating container " + dockerImage);

		final HostConfig hostConfig = HostConfig.builder()
				.appendBinds(Bind.from(solutionDir.getAbsolutePath()).to("/solution").readOnly(false).build()).build();
		ContainerConfig containerConfig = ContainerConfig.builder().image(dockerImage).hostConfig(hostConfig)
				.networkDisabled(true).build();

		ContainerCreation containerCreation;
		try {
			containerCreation = docker.createContainer(containerConfig);
		} catch (DockerException e) {
			throw new RunningInternalErrorException("Failed to create docker container.", e);
		} catch (InterruptedException e) {
			log.warn("Interrupted while creating container, probably leaking.");
			throw e;
		}
		if (containerCreation.warnings() != null) {
			for (String warning : containerCreation.warnings()) {
				log.warn(warning);
			}
		}

		log.debug("The container has been assigned id " + containerCreation.id());

		return containerCreation.id();
	}

	private void startContainer(String containerId) throws RunningInternalErrorException, InterruptedException {
		log.debug("Starting container " + containerId);

		try {
			docker.startContainer(containerId);
		} catch (DockerException e) {
			removeContainer(containerId);
			throw new RunningInternalErrorException("Failed to start docker container.", e);
		} catch (InterruptedException e) {
			log.warn("Intrurrupted while starting container.");
			removeContainer(containerId);
			throw e;
		}
	}

	private Integer waitForContainer(String containerId) {
		log.debug("Waiting for container " + containerId);

		try {
			return docker.waitContainer(containerId).statusCode();
		} catch (DockerException e) {
			log.warn("Error while waiting for container " + containerId, e);
			return -1;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return -1;
		}
	}

	private void reportFailure(String containerId, Integer statusCode)
			throws RunningFailedException, InterruptedException {
		String log = null;
		try {
			LogStream logStream = docker.logs(containerId, LogsParam.stderr(), LogsParam.stdout(), LogsParam.tail(20));
			log = logStream.readFully();
		} catch (ContainerNotFoundException e) {
			log = "The container " + containerId + " does not exist.";
		} catch (DockerException e) {
			log = e.getMessage();
		}
		throw new RunningFailedException(MessageFormat.format("Exited with code {0} with log: {1}", statusCode, log));
	}

	private void removeContainer(String containerId) throws RunningInternalErrorException, InterruptedException {
		log.debug("Removing container " + containerId);

		try {
			docker.removeContainer(containerId, RemoveContainerParam.forceKill());
		} catch (ContainerNotFoundException e) {
			// the container doesn't exist, nothing to do
		} catch (DockerException e) {
			throw new RunningInternalErrorException("Failed to remove docker container.", e);
		} catch (InterruptedException e) {
			log.warn("Intrurrupted while force removing container.");
			throw e;
		}
	}
}
