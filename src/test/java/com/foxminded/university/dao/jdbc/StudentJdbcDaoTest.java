package com.foxminded.university.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;

import com.foxminded.university.dao.jdbc.StudentJdbcDao;
import com.foxminded.university.entity.Student;
import com.foxminded.university.utils.ConnectionProvider;
import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class StudentJdbcDaoTest extends DBUnitConfig {

	private StudentJdbcDao studentJdbcDao;

	public StudentJdbcDaoTest(String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		studentJdbcDao = new StudentJdbcDao(new ConnectionProvider());
		beforeData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("scheduleData.xml"));
		tester.setDataSet(beforeData);
		tester.onSetup();
	}

	@Test
	public void testGetStudentById() throws Exception {
		Student expected = new Student(1, "Oleg", "Ivanov", 1);

		Student result = studentJdbcDao.getById(1);

		assertEquals(expected, result);
	}

	@Test
	public void testGetAllStudents() throws Exception {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1, "Oleg", "Ivanov", 1));
		expected.add(new Student(2, "Pert", "Petrov", 1));
		expected.add(new Student(3, "Kate", "Kredysheva", 2));
		expected.add(new Student(4, "Olga", "Abramova", 2));
		expected.add(new Student(5, "Pavel", "Titov", 2));

		List<Student> actual = studentJdbcDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void testSaveNewStudent() throws Exception {
		Student student = new Student();
		student.setFirstName("Maksim");
		student.setLastName("Manaev");
		student.setGroupId(2);
		studentJdbcDao.add(student);
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("students-data-save.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		String[] ignore = { "student_id" };
		assertEqualsIgnoreCols(expectedData, actualData, "students", ignore);
	}

	@Test
	public void testRemoveStudent() throws Exception {
		studentJdbcDao.remove(new Student(5, "Pavel", "Titov", 2));
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("students-data-remove.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("students"), actualData.getTable("students"));
	}

	@Test
	public void testGetStudentsByGroupId() throws Exception {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1, "Oleg", "Ivanov", 1));
		expected.add(new Student(2, "Petr", "Petrov", 1));

		List<Student> result = studentJdbcDao.getStudentsByGroupId(1);

		assertEquals(expected, result);
	}
}
