package com.foxminded.university.exception;

import com.foxminded.university.entity.Lesson;

public class GroupIsBusyException extends CreatingDayScheduleException {

	public GroupIsBusyException(Lesson lesson) {
		super(lesson);
	}

	@Override
	public String toString() {
		return "Group " + getLesson().getGroup() + " is busy!";
	}
}
