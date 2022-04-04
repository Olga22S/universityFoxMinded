package com.foxminded.university.entity;

import java.util.List;

public class MonthSchedule {

	private int id;
	private String month;
	private List<WeekSchedule> weekSchedules;

	public MonthSchedule() {

	}

	public MonthSchedule(String month, List<WeekSchedule> weekSchedules) {
		this.month = month;
		this.weekSchedules = weekSchedules;
	}

	public MonthSchedule(int id, String month, List<WeekSchedule> weekSchedules) {
		this.id = id;
		this.month = month;
		this.weekSchedules = weekSchedules;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<WeekSchedule> getWeekSchedules() {
		return weekSchedules;
	}

	public void setWeekSchedules(List<WeekSchedule> weekSchedules) {
		this.weekSchedules = weekSchedules;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MonthSchedule)) {
			return false;
		}
		MonthSchedule other = (MonthSchedule) obj;
		if (id != other.id) {
			return false;
		}
		if (month == null) {
			if (other.month != null) {
				return false;
			}
		} else if (!month.equals(other.month)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return month + System.lineSeparator() + weekSchedules;
	}
}
