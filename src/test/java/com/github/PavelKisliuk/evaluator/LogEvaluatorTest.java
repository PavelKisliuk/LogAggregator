package com.github.PavelKisliuk.evaluator;

import com.github.PavelKisliuk.model.Log;
import com.github.PavelKisliuk.predicate.LogPredicate;
import com.github.PavelKisliuk.reader.LogReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogEvaluatorTest {
	private static final String DIRECTORY = "src\\test\\resources\\logs";
	private static final String FILE_NAME = "\\log_";
	private List<Log> expectedList = new ArrayList<>(Arrays.asList(
			new Log("2019-04-01T00:00:00 Username_7 Message_22"),
			new Log("2019-04-01T00:03:00 Username_7 Message_90"),
			new Log("2019-04-01T00:05:00 Username_7 Message_92"),
			new Log("2019-04-01T00:22:00 Username_7 Message_80")
	));

	private HashMap<Integer, List<Log>> readerResult = new HashMap<>();

	private LogReader logReader = new LogReader(DIRECTORY, FILE_NAME);

	{
		logReader.run();
		for (int i = 1; i <= logReader.size(); i++) {
			readerResult.put(i, logReader.get(i));
		}
	}

	private LogPredicate logPredicate = new LogPredicate();

	{
		logPredicate.setUserName("Username_7");
	}

	private LogEvaluator logEvaluator = new LogEvaluator(readerResult, logPredicate);

	{
		logEvaluator.run();
	}

	@Test
	void EvaluatorTestUsername_7() {
		ArrayList<Log> list = new ArrayList<>();
		for (int i = 1; i <= logEvaluator.size(); i++) {
			if (!logEvaluator.get(i).isEmpty()) {
				list.addAll(logEvaluator.get(i));
			}
		}
		assertEquals(expectedList, list);
	}
}