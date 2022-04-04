package com.foxminded.university.exception;

import com.foxminded.university.entity.Lesson;

public class TeacherIsBusyException extends CreatingDayScheduleException {

	public TeacherIsBusyException(Lesson lesson) {
		super(lesson);
	}

	@Override
	public String toString() {
		return "Teacher " + getLesson().getTeacher() + " is busy!";
	}
}
