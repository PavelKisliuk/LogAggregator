/*  By Pavel Kisliuk, 01.09.2019
 *  This is class for education and nothing rights don't reserved.
 */

package com.github.PavelKisliuk.io;

import com.github.PavelKisliuk.model.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * The {@code LogWriter} class is class for
 * writing filtered log's in one file.
 * <p>
 *
 * @author Kisliuk Pavel Sergeevich
 * @since 12.0
 */
public class LogWriter {
	/**
	 * List of log's for writing to file.
	 */
	private List<Log> writeList;

	/**
	 * Constructor for data initialization.
	 * <p>
	 *
	 * @param writeList is list of {@code Log} for writing to file.
	 */
	public LogWriter(List<Log> writeList) {
		this.writeList = writeList;
	}

	/**
	 * Write log's to one file.
	 * <p>
	 *
	 * @param path is filepath.
	 */
	public void write(String path) {
		try {
			Files.write(Paths.get(path),
					(Iterable<String>) writeList
							.parallelStream()
							.map(Log::toString)::iterator);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}