package com.foxminded.university.service;

import static java.util.Objects.isNull;
import java.util.*;
import com.foxminded.university.dao.jdbc.LessonJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.EntityNotFoundException;

public class LessonService {

	private LessonJdbcDao lessonJdbcDao;

	public LessonService(LessonJdbcDao lessonJdbcDao) {
		this.lessonJdbcDao = lessonJdbcDao;
	}

	public void add(Lesson lesson) {
		lessonJdbcDao.add(lesson);
	}

	public Lesson getById(int id) {
		Lesson lesson = lessonJdbcDao.getById(id);
		if (isNull(lesson)) {
			throw new EntityNotFoundException("Lesson by id=" + id + " was not found");
		} else {
			return lesson;
		}
	}

	public void update(Lesson lesson) {
		lessonJdbcDao.update(lesson);
	}

	public void remove(Lesson lesson) {
		lessonJdbcDao.remove(lesson);
	}

	public List<Lesson> getLessons() {
		return lessonJdbcDao.getAll();
	}
}
