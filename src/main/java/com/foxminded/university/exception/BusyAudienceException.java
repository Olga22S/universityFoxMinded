package com.foxminded.university.exception;

import com.foxminded.university.entity.*;

public class BusyAudienceException extends CreatingDayScheduleException {

	public BusyAudienceException(Lesson lesson) {
		super(lesson);
	}

	@Override
	public String toString() {
		return getLesson().getAudience()+" is busy!";
	}
}
