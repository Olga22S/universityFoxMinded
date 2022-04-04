package com.foxminded.university.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.foxminded.university.service.TeacherServiceTest.TestData.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.foxminded.university.dao.jdbc.TeacherJdbcDao;
import com.foxminded.university.entity.*;

@RunWith(MockitoJUnitRunner.class)
public class TeacherServiceTest {

	@Mock
	private TeacherJdbcDao teacherJdbcDao;

	@InjectMocks
	private TeacherService teacherService;

	@Test
	public void givenTeacherId_whenGetById_thenTeacherReturned() throws SQLException, IOException {
		Teacher expected = teacher;
		when(teacherJdbcDao.getById(1)).thenReturn(expected);

		Teacher actual = teacherService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacher_whenUpdate_thenTeacherUpdated() throws SQLException, IOException {
		teacherService.update(teacher);
		verify(teacherJdbcDao).update(teacher);
	}

	@Test
	public void givenTeacher_whenAdd_thenTeacherAdded() throws SQLException, IOException {
		teacherService.add(teacher2);
		verify(teacherJdbcDao).add(teacher2);
	}

	@Test
	public void givenTeacher_whenRemove_thenTeacherRemoved() throws SQLException, IOException {
		teacherService.remove(teacher);
		verify(teacherJdbcDao).remove(teacher);
	}

	@Test
	public void whenGetTeachers_thenTeachersReturned() throws SQLException, IOException {
		List<Teacher> expected = new ArrayList<>();
		expected.add(teacher);
		expected.add(teacher2);
		when(teacherJdbcDao.getAll()).thenReturn(expected);

		List<Teacher> actual = teacherService.getTeachers();

		assertEquals(expected, actual);
	}

	interface TestData {
		Teacher teacher = new Teacher(1, "Petr", "Ivanov");
		Teacher teacher2 = new Teacher(2, "Iriva", "Kozlova");
	}
}
