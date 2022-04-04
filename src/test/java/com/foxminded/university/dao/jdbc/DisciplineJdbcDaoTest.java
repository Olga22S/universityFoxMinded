package com.foxminded.university.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;

import com.foxminded.university.dao.jdbc.DisciplineJdbcDao;
import com.foxminded.university.entity.Discipline;
import com.foxminded.university.entity.Teacher;
import com.foxminded.university.utils.ConnectionProvider;
import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class DisciplineJdbcDaoTest extends DBUnitConfig {

	private DisciplineJdbcDao disciplineJdbcDao;

	public DisciplineJdbcDaoTest(String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		disciplineJdbcDao = new DisciplineJdbcDao(new ConnectionProvider());
		beforeData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("scheduleData.xml"));
		tester.setDataSet(beforeData);
		tester.onSetup();
	}

	@Test
	public void testGetDisciplineById() throws Exception {
		Discipline expected = new Discipline(1, "Economy");

		Discipline result = disciplineJdbcDao.getById(1);

		assertEquals(expected, result);
	}

	@Test
	public void testGetAllDisciplines() throws Exception {
		List<Discipline> expected = new ArrayList<>();
		expected.add(new Discipline(1, "Economy"));
		expected.add(new Discipline(2, "Mathematics"));
		expected.add(new Discipline(3, "Informatics"));
		expected.add(new Discipline(4, "English"));
		expected.add(new Discipline(5, "Philosophy"));

		List<Discipline> actual = disciplineJdbcDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void testSaveNewDiscipline() throws Exception {
		Discipline discipline = new Discipline();
		discipline.setName("Physics");
		disciplineJdbcDao.add(discipline);
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("discipline-data-save.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		String[] ignore = { "discipline_id" };
		assertEqualsIgnoreCols(expectedData, actualData, "disciplines", ignore);
	}

	@Test
	public void testRemoveDiscipline() throws Exception {
		disciplineJdbcDao.remove(new Discipline(5, "Philosophy"));
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("discipline-data-remove.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("disciplines"), actualData.getTable("disciplines"));
	}

	@Test
	public void testGetTeacherDisciplines() throws Exception {
		List<Discipline> expected = new ArrayList<>();
		expected.add(new Discipline(2, "Mathematics"));
		expected.add(new Discipline(3, "Informatics"));

		List<Discipline> result = disciplineJdbcDao.getTeacherDisciplines(new Teacher(1, "Tamara", "Nazarova"));

		assertEquals(expected, result);
	}
}
