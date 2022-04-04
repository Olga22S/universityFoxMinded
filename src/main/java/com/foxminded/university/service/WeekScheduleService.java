package com.foxminded.university.service;

import static java.util.Objects.isNull;
import java.util.*;
import com.foxminded.university.dao.jdbc.WeekScheduleJdbcDao;
import com.foxminded.university.entity.DaySchedule;
import com.foxminded.university.entity.WeekSchedule;
import com.foxminded.university.exception.EntityNotFoundException;

public class WeekScheduleService {

	private WeekScheduleJdbcDao weekScheduleJdbcDao;

	public WeekScheduleService(WeekScheduleJdbcDao weekScheduleJdbcDao) {
		this.weekScheduleJdbcDao = weekScheduleJdbcDao;
	}

	public void add(WeekSchedule weekSchedule) {
		weekScheduleJdbcDao.add(weekSchedule);
	}

	public void addDaySchedulesInWeek(WeekSchedule weekSchedule, List<DaySchedule> daySchedules) {
		weekScheduleJdbcDao.addDaySchedulesInWeek(weekSchedule, daySchedules);
	}

	public WeekSchedule getById(int id) {
		WeekSchedule weekSchedule = weekScheduleJdbcDao.getById(id);
		if (isNull(weekSchedule)) {
			throw new EntityNotFoundException("WeekSchedule by id="+ id+" was not found");
		} else {
			return weekSchedule;
		}
	}

	public void update(WeekSchedule weekSchedule) {
		weekScheduleJdbcDao.update(weekSchedule);
	}

	public void remove(WeekSchedule weekSchedule) {
		weekScheduleJdbcDao.remove(weekSchedule);
	}

	public List<WeekSchedule> getWeekSchedules() {
		return weekScheduleJdbcDao.getAll();
	}
}
