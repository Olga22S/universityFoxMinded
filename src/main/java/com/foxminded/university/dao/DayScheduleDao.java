package com.foxminded.university.dao;

import java.time.LocalDate;
import com.foxminded.university.entity.DaySchedule;
import com.foxminded.university.entity.*;

public interface DayScheduleDao extends GenericDao<DaySchedule> {

	DaySchedule getByDate(LocalDate date);

	DaySchedule getStudentDaySchedule(Student student, LocalDate date);

	DaySchedule getTeacherDaySchedule(Teacher teacher, LocalDate date);
}
