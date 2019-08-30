/*  By Pavel Kisliuk, 30.08.2019
 *  This is class for education and nothing rights don't reserved.
 */

package com.github.PavelKisliuk.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;

/**
 * The {@code LogCreator} class for creation specify amount of logs
 * <p>
 *
 * @author Kisliuk Pavel Sergeevich
 * @since 12.0
 */
class LogCreator {
	/**
	 * Pattern for start of log name.
	 */
	private static final String FILE = "\\log_";

	/**
	 * Log extension.
	 */
	private static final String EXTENSION = ".log";

	/**
	 * Class for creation random values.
	 */
	private static final Random RANDOM = new SecureRandom();

	/**
	 * Pattern for username in log file.
	 */
	private static final String USER_NAME = "Username_";

	/**
	 * Pattern for message in log file.
	 */
	private static final String MESSAGE = "Message_";

	/**
	 * Amount of millisecond's in hour.
	 */
	private static final long HOUR_MS = 3600000L;

	/**
	 * Amount of millisecond's in minute.
	 */
	private static final long MINUTE_MS = 60000L;

	/**
	 * Start date time for log time parameter. 01 04 2019 00:00:00
	 */
	private long startTime = 1554066000000L;

	/**
	 * Log directory.
	 */
	private String directory;

	/**
	 * Constructor for initialization and directory of log's creation.
	 * <p>
	 *
	 * @param directory parameter for {@code directory} filed initialization.
	 */
	LogCreator(String directory) {
		this.directory = directory;
		try {
			Files.createDirectory(Paths.get(directory));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create specify amount of log files with specify amount of log's.
	 * <p>
	 *
	 * @param necessaryFileAmount is amount of log files.
	 * @param necessaryLogAmount is amount of log's in one file.
	 */
	void create(int necessaryFileAmount, int necessaryLogAmount) {
		int fileAmount = 0;
		while (fileAmount++ < necessaryFileAmount) {
			Path path = Paths.get(directory + FILE + fileAmount + EXTENSION);
			try {
				Files.createFile(path);
			} catch (IOException e) {
				e.printStackTrace();
			}

			try (BufferedWriter writer = Files.newBufferedWriter(path)) {
				int logAmount = 0;
				while (logAmount++ < necessaryLogAmount) {
					writer.write(createLog());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Create new log record.
	 * <p>
	 *
	 * @return new log record.
	 */
	private String createLog() {
		String time = Instant.ofEpochMilli(startTime + HOUR_MS * 3).toString().replace("Z", "");
		String user = USER_NAME + RANDOM.nextInt(10);
		String message = MESSAGE + RANDOM.nextInt(100) + "\n";
		new Thread(this::incrementTime).start();
		return String.join(" ", time, user, message);
	}

	/**
	 * Increase time to hour or minute.
	 */
	private void incrementTime() {
		startTime += (RANDOM.nextInt(10) <= 8) ? MINUTE_MS : HOUR_MS;
	}
}