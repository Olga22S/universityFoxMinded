package com.foxminded.university.exception;

import com.foxminded.university.entity.Lesson;

public abstract class CreatingDayScheduleException extends RuntimeException {

	private Lesson lesson;

	public CreatingDayScheduleException(Lesson lesson) {
		this.lesson = lesson;
	}

	public Lesson getLesson() {
		return lesson;
	}
}
