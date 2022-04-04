package com.foxminded.university.entity;

import java.time.LocalTime;

public class LessonTime {

	private int id;
	private LocalTime startTime;
	private LocalTime endTime;

	public LessonTime() {

	}

	public LessonTime(int id, LocalTime startTime, LocalTime endTime) {
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public int getId() {
		return id;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LessonTime))
			return false;
		LessonTime other = (LessonTime) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return startTime + " - " + endTime;
	}
}
