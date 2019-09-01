/*  By Pavel Kisliuk, 31.08.2019
 *  This is class for education and nothing rights don't reserved.
 */

package com.github.PavelKisliuk.io;

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

/**
 * The {@code LogReader} class is {@code Runnable} realization for
 * reading log's from files.
 * <p>
 *
 * @author Kisliuk Pavel Sergeevich
 * @since 12.0
 */
public class LogReader implements Runnable {
	/**
	 * Log extension.
	 */
	private static final String EXTENSION = ".log";

	/**
	 * Current number of file to reading.
	 */
	private static AtomicInteger fileNumber;

	/**
	 * Full amount of log files in directory for reading.
	 */
	private final int fileAmount;

	/**
	 * Name of directory with log files.
	 */
	private String directory;

	/**
	 * Name pattern of log files.
	 */
	private String fileName;

	/**
	 * Container for transfigured log's.
	 */
	private HashMap<Integer, List<Log>> logGroup = new HashMap<>();

	/**
	 * Constructor for data initialization.
	 * <p>
	 *
	 * @param directory of log files.
	 * @param fileName  is name pattern of log files.
	 */
	public LogReader(String directory, String fileName) {
		this.directory = directory;
		this.fileName = fileName;
		fileNumber = new AtomicInteger(1);
		fileAmount = Objects.requireNonNull(new File(directory).listFiles()).length;
	}

	/**
	 * Return size of log container. Size is equal amount of log files in directory.
	 * <p>
	 *
	 * @return size of log container.
	 */
	public int size() {
		return logGroup.size();
	}

	/**
	 * Return element of log container.
	 * <p>
	 *
	 * @param key for choose of element.
	 * @return element of log container.
	 */
	public List<Log> get(Integer key) {
		return logGroup.get(key);
	}

	/**
	 * Read log's from specify file, transfigure it to {@code Log} instance and put to
	 * log container.
	 */
	@Override
	public void run() {
		int currentNumber;
		while ((currentNumber = fileNumber.getAndIncrement()) <= fileAmount) {
			try (BufferedReader reader = Files.newBufferedReader(Paths.get(
					directory + fileName + currentNumber + EXTENSION))) {
				logGroup.put(currentNumber, reader.lines()
						.parallel()
						.map(Log::new)
						.collect(Collectors.toList()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}