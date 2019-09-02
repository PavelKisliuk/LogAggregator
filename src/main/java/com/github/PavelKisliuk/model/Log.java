package com.github.PavelKisliuk.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Log {
	private String userName;
	private LocalDate date;
	private LocalTime time;
	private String message;

	public Log(String log) {
		String[] logElementGroup = log.split(" ");
		String[] logDateTime = logElementGroup[0].split("T");
		date = LocalDate.parse(logDateTime[0]);
		time = LocalTime.parse(logDateTime[1]);
		userName = logElementGroup[1];
		message = logElementGroup[2];
	}

	public String getUserName() {
		return userName;
	}

	public LocalDateTime getDateTime() {
		return LocalDateTime.of(date, time);
	}

	public String getMessage() {
		return message;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Log log = (Log) o;

		if (!Objects.equals(userName, log.userName)) return false;
		if (!Objects.equals(date, log.date)) return false;
		if (!Objects.equals(time, log.time)) return false;
		return Objects.equals(message, log.message);
	}

	@Override
	public int hashCode() {
		int result = userName != null ? userName.hashCode() : 0;
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + (time != null ? time.hashCode() : 0);
		result = 31 * result + (message != null ? message.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return String.join(" ", LocalDateTime.of(date, time).toString(), userName, message);
	}
}