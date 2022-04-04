package com.foxminded.university.service;

import static java.util.Objects.isNull;
import java.util.*;
import com.foxminded.university.dao.jdbc.LessonTimeJdbcDao;
import com.foxminded.university.entity.LessonTime;
import com.foxminded.university.exception.EntityNotFoundException;

public class LessonTimeService {

	private LessonTimeJdbcDao lessonTimeJdbcDao;

	public LessonTimeService(LessonTimeJdbcDao lessonTimeJdbcDao) {
		this.lessonTimeJdbcDao = lessonTimeJdbcDao;
	}

	public void add(LessonTime lessonTime) {
		lessonTimeJdbcDao.add(lessonTime);
	}

	public LessonTime getById(int id) {
		LessonTime lessonTime = lessonTimeJdbcDao.getById(id);
		if (isNull(lessonTime)) {
			throw new EntityNotFoundException("LessonTime by id=" + id + " was not found");
		} else {
			return lessonTime;
		}
	}

	public void update(LessonTime lessonTime) {
		lessonTimeJdbcDao.update(lessonTime);
	}

	public void remove(LessonTime lessonTime) {
		lessonTimeJdbcDao.remove(lessonTime);
	}

	public List<LessonTime> getLessonsTimes() {
		return lessonTimeJdbcDao.getAll();
	}
}
