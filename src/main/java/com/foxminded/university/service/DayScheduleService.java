package com.foxminded.university.service;

import java.time.LocalDate;
import java.util.*;
import com.foxminded.university.dao.jdbc.DayScheduleJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.*;
import static java.util.Objects.isNull;

public class DayScheduleService {

	private DayScheduleJdbcDao dayScheduleJdbcDao;

	public DayScheduleService(DayScheduleJdbcDao dayScheduleJdbcDao) {
		this.dayScheduleJdbcDao = dayScheduleJdbcDao;
	}

	public void add(DaySchedule daySchedule) {
		dayScheduleJdbcDao.add(daySchedule);
	}

	public DaySchedule getByDate(LocalDate date) {
		DaySchedule daySchedule = dayScheduleJdbcDao.getByDate(date);
		if (isNull(daySchedule)) {
			throw new EntityNotFoundException("DaySchedule by date=" + date + " was not found");
		} else {
			return daySchedule;
		}
	}

	public void update(DaySchedule daySchedule) {
		dayScheduleJdbcDao.update(daySchedule);
	}

	public void remove(DaySchedule daySchedule) {
		dayScheduleJdbcDao.remove(daySchedule);
	}

	public List<DaySchedule> getDaySchedules() {
		return dayScheduleJdbcDao.getAll();
	}

	public void addLesson(Lesson lesson, DaySchedule daySchedule) {
		if (!isNull(daySchedule.getLessons())) {
			isAudienceFree(lesson, daySchedule);
			isTeacherFree(lesson, daySchedule);
			isGroupFree(lesson, daySchedule);
		}
		dayScheduleJdbcDao.addLesson(lesson, daySchedule);
	}

	public DaySchedule getStudentDaySchedule(Student student, LocalDate date) {
		DaySchedule daySchedule = dayScheduleJdbcDao.getStudentDaySchedule(student, date);
		if (isNull(daySchedule)) {
			throw new EntityNotFoundException("Student daySchedule by date=" + date + " was not found");
		} else {
			return daySchedule;
		}
	}

	public DaySchedule getTeacherDaySchedule(Teacher teacher, LocalDate date) {
		DaySchedule daySchedule = dayScheduleJdbcDao.getTeacherDaySchedule(teacher, date);
		if (isNull(daySchedule)) {
			throw new EntityNotFoundException("Teacher daySchedule by date=" + date + " was not found");
		} else {
			return daySchedule;
		}
	}

	private void isAudienceFree(Lesson lesson, DaySchedule daySchedule) {
		if (daySchedule.getLessons().stream().anyMatch(l -> l.getAudience().equals(lesson.getAudience())
				&& l.getLessonTime().equals(lesson.getLessonTime()))) {
			throw new BusyAudienceException(lesson);
		}
	}

	private void isTeacherFree(Lesson lesson, DaySchedule daySchedule) {
		if (getByDate(daySchedule.getDate()).getLessons().stream().anyMatch(
				l -> l.getTeacher().equals(lesson.getTeacher()) && l.getLessonTime().equals(lesson.getLessonTime()))) {
			throw new TeacherIsBusyException(lesson);
		}
	}

	private void isGroupFree(Lesson lesson, DaySchedule daySchedule) {
		if (getByDate(daySchedule.getDate()).getLessons().stream().anyMatch(
				l -> l.getGroup().equals(lesson.getGroup()) && l.getLessonTime().equals(lesson.getLessonTime()))) {
			throw new GroupIsBusyException(lesson);
		}
	}
}
