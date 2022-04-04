package com.foxminded.university.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.foxminded.university.service.ScheduleServiceTest.TestData.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.foxminded.university.dao.jdbc.ScheduleJdbcDao;
import com.foxminded.university.entity.*;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleServiceTest {

	@Mock
	private ScheduleJdbcDao scheduleJdbcDao;

	@InjectMocks
	private ScheduleService scheduleService;

	@Test
	public void givenScheduleYear_whenGetByYear_thenScheduleReturned() throws SQLException, IOException {
		Schedule expected = schedule;
		when(scheduleJdbcDao.getByYear(2019)).thenReturn(expected);

		Schedule actual = scheduleService.getByYear(2019);

		assertEquals(expected, actual);
	}

	@Test
	public void givenSchedule_whenUpdate_thenScheduleUpdated() throws SQLException, IOException {
		scheduleService.update(schedule);
		verify(scheduleJdbcDao).update(schedule);
	}

	@Test
	public void givenSchedule_whenAdd_thenScheduleAdded() throws SQLException, IOException {
		scheduleService.add(schedule2);
		verify(scheduleJdbcDao).add(schedule2);
	}

	@Test
	public void givenSchedule_whenRemove_thenScheduleRemoved() throws SQLException, IOException {
		scheduleService.remove(schedule);
		verify(scheduleJdbcDao).remove(schedule);
	}

	@Test
	public void whenGetSchedules_thenSchedulesReturned() throws SQLException, IOException {
		List<Schedule> expected = new ArrayList<>();
		expected.add(schedule);
		expected.add(schedule2);
		when(scheduleJdbcDao.getAll()).thenReturn(expected);

		List<Schedule> actual = scheduleService.getSchedules();

		assertEquals(expected, actual);
	}

	interface TestData {
		List<MonthSchedule> monthSchedules = new ArrayList<>();
		Schedule schedule = new Schedule(1, 2019, monthSchedules);
		Schedule schedule2 = new Schedule(2, 2020, monthSchedules);
	}
}
