package com.github.PavelKisliuk.reader;

import com.github.PavelKisliuk.model.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LogReader implements Runnable {
	/**
	 * Log extension.
	 */
	private static final String EXTENSION = ".log";
	private static AtomicInteger fileNumber;

	private final int fileAmount;
	private String directory;
	private String fileName;
	private HashMap<Integer, List<Log>> logGroup = new HashMap<>();

	public LogReader(String directory, String fileName) {
		this.directory = directory;
		this.fileName = fileName;
		fileNumber = new AtomicInteger(1);
		fileAmount = Objects.requireNonNull(new File(directory).listFiles()).length;
	}

	@Override
	public void run() {
		int currentNumber;
		while ((currentNumber = fileNumber.getAndIncrement()) <= fileAmount) {
			System.out.println(directory + fileName + currentNumber + EXTENSION);
			try (BufferedReader reader = Files.newBufferedReader(Paths.get(
					directory + fileName + currentNumber + EXTENSION))) {
				logGroup.put(currentNumber, reader.lines().map(Log::new).collect(Collectors.toList()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}