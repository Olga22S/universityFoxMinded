package com.foxminded.university.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.foxminded.university.service.WeekScheduleServiceTest.TestData.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.foxminded.university.dao.jdbc.WeekScheduleJdbcDao;
import com.foxminded.university.entity.*;

@RunWith(MockitoJUnitRunner.class)
public class WeekScheduleServiceTest {

	@Mock
	private WeekScheduleJdbcDao weekScheduleJdbcDao;

	@InjectMocks
	private WeekScheduleService weekScheduleService;

	@Test
	public void givenWeekScheduleId_whenGetById_thenWeekScheduleReturned() throws SQLException, IOException {
		WeekSchedule expected = weekSchedule;
		when(weekScheduleJdbcDao.getById(1)).thenReturn(expected);

		WeekSchedule actual = weekScheduleService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	public void givenWeekSchedule_whenUpdate_thenWeekScheduleUpdated() throws SQLException, IOException {
		weekScheduleService.update(weekSchedule);
		verify(weekScheduleJdbcDao).update(weekSchedule);
	}

	@Test
	public void givenWeekSchedule_whenAdd_thenWeekScheduleAdded() throws SQLException, IOException {
		weekScheduleService.add(weekSchedule2);
		verify(weekScheduleJdbcDao).add(weekSchedule2);
	}

	@Test
	public void givenWeekSchedule_whenRemove_thenWeekScheduleRemoved() throws SQLException, IOException {
		weekScheduleService.remove(weekSchedule);
		verify(weekScheduleJdbcDao).remove(weekSchedule);
	}

	@Test
	public void whenGetWeekSchedules_thenWeekSchedulesReturned() throws SQLException, IOException {
		List<WeekSchedule> expected = new ArrayList<>();
		expected.add(weekSchedule);
		expected.add(weekSchedule2);
		when(weekScheduleJdbcDao.getAll()).thenReturn(expected);

		List<WeekSchedule> actual = weekScheduleService.getWeekSchedules();

		assertEquals(expected, actual);
	}

	interface TestData {
		List<DaySchedule> daySchedules = new ArrayList<>();
		WeekSchedule weekSchedule = new WeekSchedule(1, 1, daySchedules);
		WeekSchedule weekSchedule2 = new WeekSchedule(2, 2, daySchedules);
	}
}
