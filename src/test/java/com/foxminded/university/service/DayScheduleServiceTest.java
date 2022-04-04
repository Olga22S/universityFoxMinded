package com.foxminded.university.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.foxminded.university.service.DayScheduleServiceTest.TestData.*;
import java.time.*;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.foxminded.university.dao.jdbc.DayScheduleJdbcDao;
import com.foxminded.university.entity.*;

@RunWith(MockitoJUnitRunner.class)
public class DayScheduleServiceTest {

	@Mock
	private DayScheduleJdbcDao dayScheduleJdbcDao;

	@InjectMocks
	private DayScheduleService dayScheduleService;

	@Test
	public void givenDayScheduleDate_whenGetByDate_thenDayScheduleReturned() {
		DaySchedule expected = daySchedule;
		when(dayScheduleJdbcDao.getByDate(date)).thenReturn(expected);

		DaySchedule actual = dayScheduleService.getByDate(date);

		assertEquals(expected, actual);
	}

	@Test
	public void givenDaySchedule_whenUpdate_thenDayScheduleUpdated() {
		dayScheduleService.update(daySchedule);
		verify(dayScheduleJdbcDao).update(daySchedule);
	}

	@Test
	public void givenDaySchedule_whenAdd_thenDayScheduleAdded() {
		dayScheduleService.add(daySchedule2);
		verify(dayScheduleJdbcDao).add(daySchedule2);
	}

	@Test
	public void givenDaySchedule_whenRemove_thenDayScheduleRemoved() {
		dayScheduleService.remove(daySchedule);
		verify(dayScheduleJdbcDao).remove(daySchedule);
	}

	@Test
	public void whenGetDaySchedules_thenDaySchedulesReturned() {
		List<DaySchedule> expected = new ArrayList<>();
		expected.add(daySchedule);
		expected.add(daySchedule2);
		when(dayScheduleJdbcDao.getAll()).thenReturn(expected);

		List<DaySchedule> actual = dayScheduleService.getDaySchedules();

		assertEquals(expected, actual);
	}

	@Test
	public void givenStudentAndDate_whenGetStudentDaySchedule_thenStudentDayScheduleReturned() {
		DaySchedule expected = daySchedule;
		when(dayScheduleJdbcDao.getStudentDaySchedule(student, date)).thenReturn(expected);

		DaySchedule actual = dayScheduleService.getStudentDaySchedule(student, date);

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacherAndDate_whenGetTeacherDaySchedule_thenTeacherDayScheduleReturned() {
		DaySchedule expected = daySchedule2;
		when(dayScheduleJdbcDao.getTeacherDaySchedule(teacher, date2)).thenReturn(expected);

		DaySchedule actual = dayScheduleService.getTeacherDaySchedule(teacher, date2);

		assertEquals(expected, actual);
	}

	interface TestData {
		LocalDate date = LocalDate.of(2019, Month.SEPTEMBER, 4);
		LocalDate date2 = LocalDate.of(2019, Month.SEPTEMBER, 6);
		Student student = new Student(1, "Oleg", "Ivanov", 1);
		Teacher teacher = new Teacher(1, "Tamara", "Nazarova");
		DaySchedule daySchedule = new DaySchedule(1, date);
		DaySchedule daySchedule2 = new DaySchedule(2, date2);
	}
}
