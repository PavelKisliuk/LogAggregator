/*  By Pavel Kisliuk, 31.08.2019
 *  This is class for education and nothing rights don't reserved.
 */

package com.github.PavelKisliuk.predicate;

import com.github.PavelKisliuk.model.Log;

import java.time.LocalDateTime;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * The {@code LogPredicate} class is {@code Predicate} realization for
 * verification {@code Log} data.
 * <p>
 *
 * @author Kisliuk Pavel Sergeevich
 * @since 12.0
 */
public class LogPredicate implements Predicate<Log> {
	/**
	 * String for username verification in {@code Log} instance.
	 */
	private String userName;

	/**
	 * Start date in diapason of verification in {@code Log} instance.
	 */
	private LocalDateTime dateBefore;

	/**
	 * Final date in diapason of verification in {@code Log} instance.
	 */
	private LocalDateTime dateAfter;

	/**
	 * Pattern for message verification in {@code Log} instance.
	 */
	private Pattern pattern;

	/**
	 * Setter for {@code userName}.
	 * <p>
	 *
	 * @param userName is data for {@code userName} initialization.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Setter for {@code dateBefore}.
	 * <p>
	 *
	 * @param dateBefore is data for {@code dateBefore} initialization.
	 */
	public void setDateBefore(LocalDateTime dateBefore) {
		this.dateBefore = dateBefore;
	}

	/**
	 * Setter for {@code dateAfter}.
	 * <p>
	 *
	 * @param dateAfter is data for {@code dateAfter} initialization.
	 */
	public void setDateAfter(LocalDateTime dateAfter) {
		this.dateAfter = dateAfter;
	}

	/**
	 * Setter for {@code pattern}.
	 * <p>
	 *
	 * @param pattern is data for {@code pattern} initialization.
	 */
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * Verify {@code Log} instance to constraint satisfaction.
	 * <p>
	 *
	 * @param log is verified parameter.
	 * @return {@code true} if constraint satisfied, otherwise return {@code false}.
	 */
	@Override
	public boolean test(Log log) {
		boolean flag = true;
		if (userName != null) {
			flag = log.getUserName().equals(userName);
		}
		if (dateBefore != null && flag) {
			flag = log.getDateTime().isAfter(dateBefore);
		}
		if (dateAfter != null && flag) {
			flag = log.getDateTime().isBefore(dateAfter);
		}
		if (pattern != null && flag) {
			flag = pattern.matcher(log.getMessage()).find();
		}
		return flag;
	}
}