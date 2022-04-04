package com.foxminded.university.entity;

import java.util.List;

public class Schedule {

	private int id;
	private int year;
	private List<MonthSchedule> monthSchedules;

	public Schedule() {

	}

	public Schedule(int year) {
		this.year = year;
	}

	public Schedule(int year, List<MonthSchedule> monthSchedules) {
		this.year = year;
		this.monthSchedules = monthSchedules;
	}

	public Schedule(int id, int year, List<MonthSchedule> monthSchedules) {
		this.id = id;
		this.year = year;
		this.monthSchedules = monthSchedules;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<MonthSchedule> getMonthSchedules() {
		return monthSchedules;
	}

	public void setMonthSchedules(List<MonthSchedule> monthSchedules) {
		this.monthSchedules = monthSchedules;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + year;
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
		if (!(obj instanceof Schedule)) {
			return false;
		}
		Schedule other = (Schedule) obj;
		if (id != other.id) {
			return false;
		}
		if (year != other.year) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Year: " + year + System.lineSeparator() + monthSchedules;
	}
}
