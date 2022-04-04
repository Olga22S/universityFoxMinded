package com.foxminded.university.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.foxminded.university.service.LessonServiceTest.TestData.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.foxminded.university.dao.jdbc.LessonJdbcDao;
import com.foxminded.university.entity.Audience;
import com.foxminded.university.entity.Discipline;
import com.foxminded.university.entity.Group;
import com.foxminded.university.entity.Lesson;
import com.foxminded.university.entity.LessonTime;
import com.foxminded.university.entity.Teacher;

@RunWith(MockitoJUnitRunner.class)
public class LessonServiceTest {

	@Mock
	private LessonJdbcDao lessonJdbcDao;

	@InjectMocks
	private LessonService lessonService;

	@Test
	public void givenLessonId_whenGetById_thenLessonReturned() throws SQLException, IOException {
		Lesson expected = lesson;
		when(lessonJdbcDao.getById(1)).thenReturn(expected);

		Lesson actual = lessonService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	public void givenLesson_whenUpdate_thenLessonUpdated() throws SQLException, IOException {
		lessonService.update(lesson);
		verify(lessonJdbcDao).update(lesson);
	}

	@Test
	public void givenLesson_whenAdd_thenLessonAdded() throws SQLException, IOException {
		lessonService.add(lesson2);
		verify(lessonJdbcDao).add(lesson2);
	}

	@Test
	public void givenLesson_whenRemove_thenLessonRemoved() throws SQLException, IOException {
		lessonService.remove(lesson);
		verify(lessonJdbcDao).remove(lesson);
	}

	@Test
	public void whenGetLessons_thenLessonsReturned() throws SQLException, IOException {
		List<Lesson> expected = new ArrayList<>();
		expected.add(lesson);
		expected.add(lesson2);
		when(lessonJdbcDao.getAll()).thenReturn(expected);

		List<Lesson> actual = lessonService.getLessons();

		assertEquals(expected, actual);
	}

	interface TestData {
		Discipline discipline = new Discipline(1, "Math");
		Audience audience = new Audience(1, 111);
		Teacher teacher = new Teacher(1, "Tamara", "Nazarova");
		Group group = new Group(1, "s1");
		LessonTime firstLessonTime = new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30));
		LessonTime secondLessonTime = new LessonTime(2, LocalTime.of(13, 00), LocalTime.of(14, 30));
		Lesson lesson = new Lesson(1, discipline, audience, teacher, group, firstLessonTime);
		Lesson lesson2 = new Lesson(2, discipline, audience, teacher, group, secondLessonTime);
	}
}
