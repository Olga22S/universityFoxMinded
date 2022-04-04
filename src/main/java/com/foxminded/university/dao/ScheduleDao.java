package com.foxminded.university.dao;

import com.foxminded.university.entity.Schedule;

public interface ScheduleDao extends GenericDao<Schedule> {

	Schedule getByYear(int year);
}
