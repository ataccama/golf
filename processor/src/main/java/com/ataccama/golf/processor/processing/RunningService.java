package com.ataccama.golf.processor.processing;

import java.io.File;

import com.ataccama.golf.processor.processing.errors.RunningException;

public interface RunningService {
	public void run(File solutionDir) throws RunningException, InterruptedException;
}
