package com.foxminded.university.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.foxminded.university.service.MonthScheduleServiceTest.TestData.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.foxminded.university.dao.jdbc.MonthScheduleJdbcDao;
import com.foxminded.university.entity.*;

@RunWith(MockitoJUnitRunner.class)
public class MonthScheduleServiceTest {

	@Mock
	private MonthScheduleJdbcDao monthScheduleJdbcDao;

	@InjectMocks
	private MonthScheduleService monthScheduleService;

	@Test
	public void givenMonth_whenGetByMonth_thenMonthScheduleReturned() throws SQLException, IOException {
		MonthSchedule expected = monthSchedule;
		when(monthScheduleJdbcDao.getByMonth(month)).thenReturn(expected);

		MonthSchedule actual = monthScheduleService.getByMonth(month);

		assertEquals(expected, actual);
	}

	@Test
	public void givenMonthSchedule_whenUpdate_thenMonthScheduleUpdated() throws SQLException, IOException {
		monthScheduleService.update(monthSchedule);
		verify(monthScheduleJdbcDao).update(monthSchedule);
	}

	@Test
	public void givenMonthSchedule_whenAdd_thenMonthScheduleAdded() throws SQLException, IOException {
		monthScheduleService.add(monthSchedule2);
		verify(monthScheduleJdbcDao).add(monthSchedule2);
	}

	@Test
	public void givenMonthSchedule_whenRemove_thenMonthScheduleRemoved() throws SQLException, IOException {
		monthScheduleService.remove(monthSchedule);
		verify(monthScheduleJdbcDao).remove(monthSchedule);
	}

	@Test
	public void whenGetMonthSchedules_thenMonthSchedulesReturned() throws SQLException, IOException {
		List<MonthSchedule> expected = new ArrayList<>();
		expected.add(monthSchedule);
		expected.add(monthSchedule2);
		when(monthScheduleJdbcDao.getAll()).thenReturn(expected);

		List<MonthSchedule> actual = monthScheduleService.getMonthSchedules();

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacherAndMonth_whenGetTeacherMonthSchedule_thenTeacherMonthScheduleReturned()
			throws SQLException, IOException {
		MonthSchedule expected = monthSchedule;
		when(monthScheduleJdbcDao.getTeacherMonthSchedule(teacher, month)).thenReturn(expected);

		MonthSchedule actual = monthScheduleService.getTeacherMonthSchedule(teacher, month);

		assertEquals(expected, actual);
	}

	@Test
	public void givenStudentAndMonth_whenGetStudentMonthSchedule_thenStudentMonthScheduleReturned()
			throws SQLException, IOException {
		MonthSchedule expected = monthSchedule;
		when(monthScheduleJdbcDao.getStudentMonthSchedule(student, month)).thenReturn(expected);

		MonthSchedule actual = monthScheduleService.getStudentMonthSchedule(student, month);

		assertEquals(expected, actual);
	}

	interface TestData {
		String month = "September";
		String month2 = "October";
		Teacher teacher = new Teacher(1, "Tamara", "Nazarova");
		Student student = new Student(1, "Olga", "Pyshkina", 1);
		List<WeekSchedule> weekSchedules = new ArrayList<>();
		MonthSchedule monthSchedule = new MonthSchedule(1, month, weekSchedules);
		MonthSchedule monthSchedule2 = new MonthSchedule(2, month2, weekSchedules);
	}
}
