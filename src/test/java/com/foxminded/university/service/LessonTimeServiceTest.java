package com.foxminded.university.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.foxminded.university.service.LessonTimeServiceTest.TestData.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.foxminded.university.dao.jdbc.LessonTimeJdbcDao;
import com.foxminded.university.entity.LessonTime;

@RunWith(MockitoJUnitRunner.class)
public class LessonTimeServiceTest {

	@Mock
	private LessonTimeJdbcDao lessonTimeJdbcDao;

	@InjectMocks
	private LessonTimeService lessonTimeService;

	@Test
	public void givenLessonTimeId_whenGetById_thenLessonTimeReturned() throws SQLException, IOException {
		LessonTime expected = lessonTime;
		when(lessonTimeJdbcDao.getById(1)).thenReturn(expected);

		LessonTime actual = lessonTimeService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	public void givenLessonTime_whenUpdate_thenLessonTimeUpdated() throws SQLException, IOException {
		lessonTimeService.update(lessonTime);
		verify(lessonTimeJdbcDao).update(lessonTime);
	}

	@Test
	public void givenLessonTime_whenAdd_thenLessonTimeAdded() throws SQLException, IOException {
		lessonTimeService.add(lessonTime2);
		verify(lessonTimeJdbcDao).add(lessonTime2);
	}

	@Test
	public void givenLessonTime_whenRemove_thenLessonTimeRemoved() throws SQLException, IOException {
		lessonTimeService.remove(lessonTime);
		verify(lessonTimeJdbcDao).remove(lessonTime);
	}

	@Test
	public void whenGetLessonTimes_thenLessonTimesReturned() throws SQLException, IOException {
		List<LessonTime> expected = new ArrayList<>();
		expected.add(lessonTime);
		expected.add(lessonTime2);
		when(lessonTimeJdbcDao.getAll()).thenReturn(expected);

		List<LessonTime> actual = lessonTimeService.getLessonsTimes();

		assertEquals(expected, actual);
	}

	interface TestData {
		LessonTime lessonTime = new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30));
		LessonTime lessonTime2 = new LessonTime(2, LocalTime.of(13, 00), LocalTime.of(14, 30));
	}
}
