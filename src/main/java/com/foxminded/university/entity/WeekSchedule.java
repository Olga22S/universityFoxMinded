package com.foxminded.university.entity;

import java.util.List;

public class WeekSchedule {

	private int id;
	private int weekNumber;
	private List<DaySchedule> daySchedules;

	public WeekSchedule() {

	}

	public WeekSchedule(int weekNumber) {
		this.weekNumber = weekNumber;
	}

	public WeekSchedule(int weekNumber, List<DaySchedule> daySchedules) {
		this.weekNumber = weekNumber;
		this.daySchedules = daySchedules;
	}

	public WeekSchedule(int id, int weekNumber, List<DaySchedule> daySchedules) {
		this.id = id;
		this.weekNumber = weekNumber;
		this.daySchedules = daySchedules;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(int weekNumber) {
		this.weekNumber = weekNumber;
	}

	public List<DaySchedule> getDaySchedules() {
		return daySchedules;
	}

	public void setDaySchedules(List<DaySchedule> daySchedules) {
		this.daySchedules = daySchedules;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((daySchedules == null) ? 0 : daySchedules.hashCode());
		result = prime * result + id;
		result = prime * result + weekNumber;
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
		if (!(obj instanceof WeekSchedule)) {
			return false;
		}
		WeekSchedule other = (WeekSchedule) obj;
		if (daySchedules == null) {
			if (other.daySchedules != null) {
				return false;
			}
		} else if (!daySchedules.equals(other.daySchedules)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (weekNumber != other.weekNumber) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "id= "+id +" weekNumber= "+ weekNumber+" "+ daySchedules + "";
	}
}
