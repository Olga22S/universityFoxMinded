package com.foxminded.university.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.foxminded.university.service.DisciplineServiceTest.TestData.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.foxminded.university.dao.jdbc.DisciplineJdbcDao;
import com.foxminded.university.entity.Discipline;
import com.foxminded.university.entity.Teacher;

@RunWith(MockitoJUnitRunner.class)
public class DisciplineServiceTest {

	@Mock
	private DisciplineJdbcDao disciplineJdbcDao;

	@InjectMocks
	private DisciplineService disciplineService;

	@Test
	public void givenDisciplineId_whenGetById_thenDisciplineReturned() throws SQLException, IOException {
		Discipline expected = discipline;
		when(disciplineJdbcDao.getById(1)).thenReturn(expected);

		Discipline actual = disciplineService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	public void givenDiscipline_whenUpdate_thenDisciplineUpdated() throws SQLException, IOException {
		disciplineService.update(discipline);
		verify(disciplineJdbcDao).update(discipline);
	}

	@Test
	public void givenDiscipline_whenAdd_thenDisciplineAdded() throws SQLException, IOException {
		disciplineService.add(discipline2);
		verify(disciplineJdbcDao).add(discipline2);
	}

	@Test
	public void givenDiscipline_whenRemove_thenDisciplineRemoved() throws SQLException, IOException {
		disciplineService.remove(discipline);
		verify(disciplineJdbcDao).remove(discipline);
	}

	@Test
	public void whenGetDisciplines_thenDisciplinesReturned() throws SQLException, IOException {
		List<Discipline> expected = new ArrayList<>();
		expected.add(discipline);
		expected.add(discipline2);
		when(disciplineJdbcDao.getAll()).thenReturn(expected);

		List<Discipline> actual = disciplineService.getDisciplines();

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacher_whenGetTeacherDisciplines_thenTeacherDisciplinesReturned()
			throws SQLException, IOException {
		Teacher teacher = new Teacher(1, "Petr", "Petrov");
		List<Discipline> expected = new ArrayList<>();
		expected.add(discipline);
		expected.add(discipline2);
		when(disciplineJdbcDao.getTeacherDisciplines(teacher)).thenReturn(expected);

		List<Discipline> actual = disciplineService.getTeacherDisciplines(teacher);

		assertEquals(expected, actual);
	}

	interface TestData {
		Discipline discipline = new Discipline(1, "Economy");
		Discipline discipline2 = new Discipline(2, "Math");
	}
}
