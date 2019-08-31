/*  By Pavel Kisliuk, 31.08.2019
 *  This is class for education and nothing rights don't reserved.
 */

package com.github.PavelKisliuk.evaluator;

import com.github.PavelKisliuk.model.Log;
import com.github.PavelKisliuk.predicate.LogPredicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * The {@code LogEvaluator} class is {@code Runnable} realization for
 * filtering log's.
 * <p>
 *
 * @author Kisliuk Pavel Sergeevich
 * @since 12.0
 */
public class LogEvaluator implements Runnable {
	/**
	 * Number of position of {@code List} of log's in log container.
	 */
	private static AtomicInteger logGroupPosition;

	/**
	 * Log container.
	 */
	private HashMap<Integer, List<Log>> logGroup;

	/**
	 * Predicate fo filtering of log's.
	 */
	private LogPredicate logPredicate;

	/**
	 * Constructor for data initialization.
	 * <p>
	 *
	 * @param logGroup     is container of log's.
	 * @param logPredicate is predicate for log's filtering.
	 */
	public LogEvaluator(HashMap<Integer, List<Log>> logGroup, LogPredicate logPredicate) {
		logGroupPosition = new AtomicInteger(1);
		this.logGroup = logGroup;
		this.logPredicate = logPredicate;
	}

	/**
	 * Return size of log container.
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
	 * Reposition {@code Log} instance to one list.
	 * <p>
	 *
	 * @return {@code List} of log's.
	 */
	public List<Log> toList() {
		ArrayList<Log> list = new ArrayList<>();
		for(int i = 1; i <= logGroup.size(); i++) {
			if(!logGroup.get(i).isEmpty()) {
				list.addAll(logGroup.get(i));
			}
		}
		return list;
	}

	/**
	 * Take {@code List} of log's from log's container, filter it and rewrite
	 * in log's container by the same position.
	 */
	@Override
	public void run() {
		int currentNumber;
		while ((currentNumber = logGroupPosition.getAndIncrement()) <= logGroup.size()) {
			List<Log> logList = logGroup.get(currentNumber);
			logGroup.put(currentNumber, logList.parallelStream()
					.filter(logPredicate)
					.collect(Collectors.toList()));
		}
	}
}