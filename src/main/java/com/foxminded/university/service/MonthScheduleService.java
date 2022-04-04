package com.foxminded.university.service;

import static java.util.Objects.isNull;
import java.util.*;
import com.foxminded.university.dao.jdbc.MonthScheduleJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.EntityNotFoundException;

public class MonthScheduleService {

	private MonthScheduleJdbcDao monthScheduleJdbcDao;

	public MonthScheduleService(MonthScheduleJdbcDao monthScheduleJdbcDao) {
		this.monthScheduleJdbcDao = monthScheduleJdbcDao;
	}

	public void add(MonthSchedule monthSchedule) {
		monthScheduleJdbcDao.add(monthSchedule);
	}

	public void addWeekSchedulesInMonth(MonthSchedule monthSchedule, List<WeekSchedule> weekSchedules) {
		monthScheduleJdbcDao.addWeekSchedulesInMonth(monthSchedule, weekSchedules);
	}

	public MonthSchedule getByMonth(String month) {
		MonthSchedule monthSchedule = monthScheduleJdbcDao.getByMonth(month);
		if (isNull(monthSchedule)) {
			throw new EntityNotFoundException("MonthSchedule by month=" + month + " was not found");
		} else {
			return monthSchedule;
		}
	}

	public void update(MonthSchedule monthSchedule) {
		monthScheduleJdbcDao.update(monthSchedule);
	}

	public void remove(MonthSchedule monthSchedule) {
		monthScheduleJdbcDao.remove(monthSchedule);
	}

	public List<MonthSchedule> getMonthSchedules() {
		return monthScheduleJdbcDao.getAll();
	}

	public MonthSchedule getTeacherMonthSchedule(Teacher teacher, String month) {
		MonthSchedule monthSchedule = monthScheduleJdbcDao.getTeacherMonthSchedule(teacher, month);
		if (isNull(monthSchedule)) {
			throw new EntityNotFoundException("Teacher monthSchedule by month=" + month + " was not found");
		} else {
			return monthSchedule;
		}
	}

	public MonthSchedule getStudentMonthSchedule(Student student, String month) {
		MonthSchedule monthSchedule = monthScheduleJdbcDao.getStudentMonthSchedule(student, month);
		if (isNull(monthSchedule)) {
			throw new EntityNotFoundException("Student monthSchedule by month=" + month + " was not found");
		} else {
			return monthSchedule;
		}
	}
}
