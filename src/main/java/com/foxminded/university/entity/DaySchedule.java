package com.foxminded.university.entity;

import java.time.LocalDate;
import java.util.List;

public class DaySchedule {

	private int id;
	private LocalDate date;
	private List<Lesson> lessons;

	public DaySchedule() {

	}

	public DaySchedule(LocalDate date) {
		this.date = date;
	}

	public DaySchedule(int id, LocalDate date) {
		this.id = id;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		result = prime * result + ((lessons == null) ? 0 : lessons.hashCode());
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
		if (!(obj instanceof DaySchedule)) {
			return false;
		}
		DaySchedule other = (DaySchedule) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (lessons == null) {
			if (other.lessons != null) {
				return false;
			}
		} else if (!lessons.equals(other.lessons)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return date + " " + lessons;
	}
}
