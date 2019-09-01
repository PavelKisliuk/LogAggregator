/*  By Pavel Kisliuk, 01.09.2019
 *  This is class for education and nothing rights don't reserved.
 */

package com.github.PavelKisliuk.util;

import com.github.PavelKisliuk.model.Log;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The {@code LogStatistic} class is class for
 * log statistic creation.
 * <p>
 *
 * @author Kisliuk Pavel Sergeevich
 * @since 12.0
 */
public class LogStatistic {
	/**
	 * List of {@code Log} for statistic creation.
	 */
	private List<Log> logList;

	/**
	 * Map for statistic storing.
	 */
	private TreeMap<String, Integer> mapStatistic;

	/**
	 * Field for keeping keys of map.
	 */
	private LocalDateTime moment;

	/**
	 * Period for time statistic.
	 */
	private TimePeriodType timePeriodType;

	/**
	 * Enum of possible time period.
	 */
	enum TimePeriodType {
		HOUR, DAY, WEEK, MONTH, YEAR
	}

	/**
	 * Constructor for data initialization.
	 * <p>
	 *
	 * @param logList for statistic creation
	 */
	public LogStatistic(List<Log> logList) {
		this.logList = logList;
	}

	/**
	 * Create statistic of amount record's for every user and return it.
	 * <p>
	 *
	 * @return statistic of amount record's for every user.
	 */
	public Map<String, Integer> nameStatistic() {
		mapStatistic = new TreeMap<>();
		logList.forEach(this::countUserRecord);
		return mapStatistic;
	}

	/**
	 * Create statistic of amount record's in specify time period and return it.
	 * <p>
	 *
	 * @param timePeriodType is period of time for differentiation log's.
	 * @return statistic of amount record's in specify time period.
	 */
	public Map<String, Integer> timeStatistic(TimePeriodType timePeriodType) {
		this.timePeriodType = timePeriodType;
		mapStatistic = new TreeMap<>();
		moment = logList.get(0).getDateTime();
		mapStatistic.put(moment.toString(), 0);
		logList.forEach(this::countTimeRecord);
		return mapStatistic;
	}

	/**
	 * Increase amount record's by user in map to 1, or create new map element.
	 * <p>
	 *
	 * @param log for statistic record.
	 */
	private void countUserRecord(Log log) {
		String userName = log.getUserName();
		if (!mapStatistic.containsKey(userName)) {
			mapStatistic.put(userName, 1);
		} else {
			mapStatistic.put(userName, mapStatistic.get(userName) + 1);
		}
	}

	/**
	 * Increase amount record's by time period in map to 1, or create new map element.
	 * <p>
	 *
	 * @param log for statistic record.
	 */
	private void countTimeRecord(Log log) {
		switch (timePeriodType) {
			case HOUR:
				if (moment.getHour() != log.getDateTime().getHour()) {
					moment = log.getDateTime();
					mapStatistic.put(moment.toString(), 1);
				} else {
					mapStatistic.put(moment.toString(), mapStatistic.get(moment.toString()) + 1);
				}
				break;
			case DAY:
				if (moment.getDayOfMonth() != log.getDateTime().getDayOfMonth()) {
					moment = log.getDateTime();
					mapStatistic.put(moment.toString(), 1);
				} else {
					mapStatistic.put(moment.toString(), mapStatistic.get(moment.toString()) + 1);
				}
				break;
			case WEEK:
				if (moment.getDayOfWeek() != log.getDateTime().getDayOfWeek()) {
					moment = log.getDateTime();
					mapStatistic.put(moment.toString(), 1);
				} else {
					mapStatistic.put(moment.toString(), mapStatistic.get(moment.toString()) + 1);
				}
				break;
			case MONTH:
				if (moment.getMonth() != log.getDateTime().getMonth()) {
					moment = log.getDateTime();
					mapStatistic.put(moment.toString(), 1);
				} else {
					mapStatistic.put(moment.toString(), mapStatistic.get(moment.toString()) + 1);
				}
				break;
			case YEAR:
				if (moment.getYear() != log.getDateTime().getYear()) {
					moment = log.getDateTime();
					mapStatistic.put(moment.toString(), 1);
				} else {
					mapStatistic.put(moment.toString(), mapStatistic.get(moment.toString()) + 1);
				}
				break;
			default:
				throw new EnumConstantNotPresentException(TimePeriodType.class,
						"Incorrect TimePeriodType in LogStatistic -> timeStatistic(TimePeriodType)");
		}
	}
}