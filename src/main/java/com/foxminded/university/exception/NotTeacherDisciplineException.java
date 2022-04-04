package com.foxminded.university.exception;

import com.foxminded.university.entity.*;

public class NotTeacherDisciplineException extends RuntimeException {

	private Teacher teacher;
	private Discipline discipline;

	public NotTeacherDisciplineException(Teacher teacher, Discipline discipline) {
		this.teacher = teacher;
		this.discipline = discipline;
	}

	@Override
	public String toString() {
		return teacher + " can't teach " + discipline;
	}
}
