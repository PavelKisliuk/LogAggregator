package com.github.PavelKisliuk.reader;

import com.github.PavelKisliuk.model.Log;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogReaderTest {
	private static final String DIRECTORY = "src\\test\\resources\\logs";
	private static final String FILE_NAME = "\\log_";
	private HashMap<Integer, List<Log>> logGroup = new HashMap<>();

	{
		ArrayList<Log> list = new ArrayList<>(Arrays.asList(
				new Log("2019-04-01T00:00:00 Username_1 Message_49"),
				new Log("2019-04-01T00:00:00 Username_7 Message_22"),
				new Log("2019-04-01T00:01:00 Username_0 Message_89"),
				new Log("2019-04-01T00:02:00 Username_5 Message_94"),
				new Log("2019-04-01T00:03:00 Username_7 Message_90")
		));
		logGroup.put(1, list);

		list = new ArrayList<>(Arrays.asList(
				new Log("2019-04-01T00:05:00 Username_0 Message_89"),
				new Log("2019-04-01T00:05:00 Username_4 Message_40"),
				new Log("2019-04-01T00:05:00 Username_7 Message_92"),
				new Log("2019-04-01T00:06:00 Username_5 Message_99"),
				new Log("2019-04-01T00:07:00 Username_2 Message_26")
		));
		logGroup.put(2, list);

		list = new ArrayList<>(Arrays.asList(
				new Log("2019-04-01T00:09:00 Username_9 Message_36"),
				new Log("2019-04-01T00:09:00 Username_1 Message_17"),
				new Log("2019-04-01T00:10:00 Username_5 Message_97"),
				new Log("2019-04-01T00:11:00 Username_9 Message_27"),
				new Log("2019-04-01T00:12:00 Username_0 Message_51")
		));
		logGroup.put(3, list);

		list = new ArrayList<>(Arrays.asList(
				new Log("2019-04-01T00:14:00 Username_0 Message_37"),
				new Log("2019-04-01T00:14:00 Username_8 Message_89"),
				new Log("2019-04-01T00:15:00 Username_1 Message_5"),
				new Log("2019-04-01T00:16:00 Username_4 Message_58"),
				new Log("2019-04-01T00:17:00 Username_3 Message_6")
		));
		logGroup.put(4, list);

		list = new ArrayList<>(Arrays.asList(
				new Log("2019-04-01T00:19:00 Username_2 Message_40"),
				new Log("2019-04-01T00:19:00 Username_4 Message_29"),
				new Log("2019-04-01T00:20:00 Username_9 Message_97"),
				new Log("2019-04-01T00:21:00 Username_4 Message_38"),
				new Log("2019-04-01T00:22:00 Username_7 Message_80")
		));
		logGroup.put(5, list);
	}

	private LogReader logReader = new LogReader(DIRECTORY, FILE_NAME);

	{
		logReader.run();
	}

	@Test
	void ReaderTestSize() {
		assertEquals(logGroup.size(), logReader.size());
	}

	@Test
	void ReaderTest1() {
		int key = 1;
		assertEquals(logGroup.get(key), logReader.get(key));
	}

	@Test
	void ReaderTest2() {
		int key = 2;
		assertEquals(logGroup.get(key), logReader.get(key));
	}

	@Test
	void ReaderTest3() {
		int key = 3;
		assertEquals(logGroup.get(key), logReader.get(key));
	}

	@Test
	void ReaderTest4() {
		int key = 4;
		assertEquals(logGroup.get(key), logReader.get(key));
	}

	@Test
	void ReaderTest5() {
		int key = 5;
		assertEquals(logGroup.get(key), logReader.get(key));
	}
}