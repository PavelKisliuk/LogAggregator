package com.github.PavelKisliuk.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

import static java.nio.file.FileVisitResult.CONTINUE;
import static org.junit.jupiter.api.Assertions.*;

class LogCreatorTest {
	private static final String DIRECTORY = "src\\test\\resources\\logs";
	private static final Path PATH = Paths.get(DIRECTORY);

	@Test
	void create() throws IOException {
		deleteDirectory();
		LogCreator logCreator = new LogCreator(DIRECTORY);
		int necessaryFileAmount = 5;
		int necessaryLogAmount = 5;
		logCreator.create(necessaryFileAmount, necessaryLogAmount);
		int fileAmount = Objects.requireNonNull(new File(DIRECTORY).listFiles()).length;

		assertEquals(fileAmount, necessaryFileAmount);
	}

	private void deleteDirectory() throws IOException {
		if(!Files.isDirectory(PATH)) {
			return;
		}

		Files.walkFileTree(PATH, new SimpleFileVisitor<>() {

			@Override
			public FileVisitResult visitFile(Path file,
											 BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir,
													  IOException exc) throws IOException {
				if (exc == null) {
					Files.delete(dir);
					return CONTINUE;
				} else {
					throw exc;
				}
			}
		});
	}
}