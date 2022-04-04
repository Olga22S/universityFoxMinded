package com.foxminded.university.service;

import static java.util.Objects.isNull;
import java.util.*;
import com.foxminded.university.dao.jdbc.ScheduleJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.EntityNotFoundException;

public class ScheduleService {

	private ScheduleJdbcDao scheduleJdbcDao;

	public ScheduleService(ScheduleJdbcDao scheduleJdbcDao) {
		this.scheduleJdbcDao = scheduleJdbcDao;
	}

	public void add(Schedule schedule) {
		scheduleJdbcDao.add(schedule);
	}

	public void addMonthsInSchedule(Schedule schedule, List<MonthSchedule> monthSchedules) {
		scheduleJdbcDao.addMonthsInSchedule(schedule, monthSchedules);
	}

	public Schedule getByYear(int year) {
		Schedule schedule = scheduleJdbcDao.getByYear(year);
		if (isNull(schedule)) {
			throw new EntityNotFoundException("Schedule by year=" + year + " was not found");
		} else {
			return schedule;
		}
	}

	public void update(Schedule schedule) {
		scheduleJdbcDao.update(schedule);
	}

	public void remove(Schedule schedule) {
		scheduleJdbcDao.remove(schedule);
	}

	public List<Schedule> getSchedules() {
		return scheduleJdbcDao.getAll();
	}
}
