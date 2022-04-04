package com.foxminded.university.dao;

import com.foxminded.university.entity.*;

public interface MonthScheduleDao extends GenericDao<MonthSchedule> {

	MonthSchedule getByMonth(String month);

	MonthSchedule getTeacherMonthSchedule(Teacher teacher, String month);

	MonthSchedule getStudentMonthSchedule(Student student, String month);
}
