package com.foxminded.university.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.foxminded.university.service.StudentServiceTest.TestData.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.foxminded.university.dao.jdbc.StudentJdbcDao;
import com.foxminded.university.entity.Student;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {

	@Mock
	private StudentJdbcDao studentJdbcDao;

	@InjectMocks
	private StudentService studentService;

	@Test
	public void givenStudentId_whenGetById_thenStudentReturned() throws SQLException, IOException {
		Student expected = student;
		when(studentJdbcDao.getById(1)).thenReturn(expected);

		Student actual = studentService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	public void givenStudent_whenUpdate_thenStudentUpdated() throws SQLException, IOException {
		studentService.update(student);
		verify(studentJdbcDao).update(student);
	}

	@Test
	public void givenStudent_whenAdd_thenStudentAdded() throws SQLException, IOException {
		studentService.add(student2);
		verify(studentJdbcDao).add(student2);
	}

	@Test
	public void givenStudent_whenRemove_thenStudentRemoved() throws SQLException, IOException {
		studentService.remove(student);
		verify(studentJdbcDao).remove(student);
	}

	@Test
	public void whenGetStudents_thenStudentsReturned() throws SQLException, IOException {
		List<Student> expected = new ArrayList<>();
		expected.add(student);
		expected.add(student2);
		when(studentJdbcDao.getAll()).thenReturn(expected);

		List<Student> actual = studentService.getStudents();

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroupId_thenGetStudentsByGroupId_thenGroupStudentsReturned() throws SQLException, IOException {
		List<Student> expected = new ArrayList<>();
		expected.add(student);
		when(studentJdbcDao.getStudentsByGroupId(1)).thenReturn(expected);

		List<Student> actual = studentService.getStudentsByGroupId(1);

		assertEquals(expected, actual);
	}

	interface TestData {
		Student student = new Student(1, "Petr", "Ivanov", 1);
		Student student2 = new Student(2, "Pavel", "Koval", 2);
	}
}
