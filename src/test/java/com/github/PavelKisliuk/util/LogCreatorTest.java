package com.github.PavelKisliuk.util;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class LogCreatorTest {
	private static final LogCreator LOG_CREATOR = new LogCreator("logs");
	@Test
	void create() {
		int necessaryFileAmount = 1000;
		int necessaryLogAmount = 100;
		LOG_CREATOR.create(1000, 100);

		fail();
	}
}