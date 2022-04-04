package com.foxminded.university.dao.jdbc;

import java.util.*;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;

import com.foxminded.university.dao.jdbc.TeacherJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.utils.ConnectionProvider;
import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class TeacherJdbcDaoTest extends DBUnitConfig {

	private TeacherJdbcDao teacherJdbcDao;

	public TeacherJdbcDaoTest(String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		teacherJdbcDao = new TeacherJdbcDao(new ConnectionProvider());
		beforeData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("scheduleData.xml"));
		tester.setDataSet(beforeData);
		tester.onSetup();
	}

	@Test
	public void testGetTeacherById() throws Exception {
		Teacher expected = new Teacher(1, "Tamara", "Nazarova");
		Discipline discipline1 = new Discipline(2, "Mathematics");
		Discipline discipline2 = new Discipline(3, "Informatics");
		List<Discipline> disciplines = new ArrayList<>();
		disciplines.add(discipline1);
		disciplines.add(discipline2);
		expected.setDisciplines(disciplines);

		Teacher result = teacherJdbcDao.getById(1);

		assertEquals(expected, result);
	}

	@Test
	public void testGetAllTeachers() throws Exception {
		List<Teacher> expected = new ArrayList<>();
		expected.add(new Teacher(1, "Tamara", "Nazarova"));
		expected.add(new Teacher(2, "Olga", "Petrova"));
		expected.add(new Teacher(3, "Sergey", "Koval"));
		expected.add(new Teacher(4, "Elena", "Tolstova"));
		expected.add(new Teacher(5, "Ilya", "Titov"));

		List<Teacher> actual = teacherJdbcDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void testSaveNewTeacher() throws Exception {
		Teacher teacher = new Teacher();
		teacher.setFirstName("Oleg");
		teacher.setLastName("Olegovich");
		teacherJdbcDao.add(teacher);
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("teachers-data-save.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		String[] ignore = { "teacher_id" };
		assertEqualsIgnoreCols(expectedData, actualData, "teachers", ignore);
	}

	@Test
	public void testRemoveTeacher() throws Exception {
		teacherJdbcDao.remove(new Teacher(5, "Ilya", "Titov"));
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("teachers-data-remove.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("teachers"), actualData.getTable("teachers"));
	}

	@Test
	public void testAddDisciplinesForTeacher() throws Exception {
		Discipline discipline1 = new Discipline(1, "Economy");
		Discipline discipline2 = new Discipline(2, "Mathematics");
		List<Discipline> disciplines = new ArrayList<>();
		disciplines.add(discipline1);
		disciplines.add(discipline2);
		teacherJdbcDao.addDisciplinesForTeacher(new Teacher(5, "Ilya", "Titov"), disciplines);
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("teachers-addDisciplinesForTeacher.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("teachers_disciplines"),
				actualData.getTable("teachers_disciplines"));
	}
}
