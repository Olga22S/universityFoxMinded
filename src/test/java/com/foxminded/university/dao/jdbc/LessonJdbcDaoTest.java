package com.foxminded.university.dao.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;

import com.foxminded.university.dao.jdbc.LessonJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.NotTeacherDisciplineException;
import com.foxminded.university.utils.ConnectionProvider;
import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class LessonJdbcDaoTest extends DBUnitConfig {

	private ConnectionProvider connectionProvider;
	private AudienceJdbcDao audienceJdbcDao;
	private DisciplineJdbcDao disciplineJdbcDao;
	private LessonTimeJdbcDao lessonTimeJdbcDao;
	private GroupJdbcDao groupJdbcDao;
	private TeacherJdbcDao teacherJdbcDao;
	private LessonJdbcDao lessonJdbcDao;

	public LessonJdbcDaoTest(String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		connectionProvider = new ConnectionProvider();
		audienceJdbcDao = new AudienceJdbcDao(connectionProvider);
		disciplineJdbcDao = new DisciplineJdbcDao(connectionProvider);
		lessonTimeJdbcDao = new LessonTimeJdbcDao(connectionProvider);
		groupJdbcDao = new GroupJdbcDao(connectionProvider);
		teacherJdbcDao = new TeacherJdbcDao(connectionProvider);
		lessonJdbcDao = new LessonJdbcDao(connectionProvider, lessonTimeJdbcDao, disciplineJdbcDao, audienceJdbcDao,
				groupJdbcDao, teacherJdbcDao);
		beforeData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("scheduleData.xml"));
		tester.setDataSet(beforeData);
		tester.onSetup();
	}

	@Test
	public void testGetLessonById() throws Exception {
		Lesson expected = new Lesson(1, new Discipline(1, "Economy"), new Audience(1, 111),
				new Teacher(2, "Olga", "Petrova"), new Group(1, "S111"),
				new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30)));

		Lesson result = lessonJdbcDao.getById(1);

		assertEquals(expected, result);
	}

	@Test
	public void testGetAllLessons() throws Exception {
		List<Lesson> expected = new ArrayList<>();
		expected.add(
				new Lesson(1, new Discipline(1, "Economy"), new Audience(1, 111), new Teacher(2, "Olga", "Petrova"),
						new Group(1, "S111"), new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30))));
		expected.add(new Lesson(2, new Discipline(2, "Mathematics"), new Audience(2, 222),
				new Teacher(1, "Tamara", "Nazarova"), new Group(2, "S222"),
				new LessonTime(2, LocalTime.of(10, 30), LocalTime.of(12, 00))));
		expected.add(
				new Lesson(3, new Discipline(3, "Informatics"), new Audience(2, 222), new Teacher(3, "Sergey", "Koval"),
						new Group(2, "S222"), new LessonTime(3, LocalTime.of(12, 30), LocalTime.of(14, 00))));
		expected.add(
				new Lesson(4, new Discipline(1, "Economy"), new Audience(1, 111), new Teacher(3, "Sergey", "Koval"),
						new Group(1, "S111"), new LessonTime(2, LocalTime.of(10, 30), LocalTime.of(12, 00))));

		List<Lesson> actual = lessonJdbcDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void testSaveNewLesson() throws Exception {
		Lesson lesson = new Lesson();
		lesson.setAudience(new Audience(2, 222));
		lesson.setDiscipline(new Discipline(1, "Economy"));
		lesson.setGroup(new Group(1, "S111"));
		lesson.setLessonTime(new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(00, 00)));
		lesson.setTeacher(new Teacher(2, "Olga", "Petrova"));
		lessonJdbcDao.add(lesson);
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("lessons-data-save.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		String[] ignore = { "lesson_id" };
		assertEqualsIgnoreCols(expectedData, actualData, "lessons", ignore);
	}

	@Test
	public void testRemoveLesson() throws Exception {
		lessonJdbcDao.remove(
				new Lesson(4, new Discipline(1, "Economy"), new Audience(1, 111), new Teacher(3, "Sergey", "Koval"),
						new Group(1, "S111"), new LessonTime(2, LocalTime.of(10, 30), LocalTime.of(12, 00))));
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("lessons-data-remove.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("lessons"), actualData.getTable("lessons"));
	}

	@Test(expected = NotTeacherDisciplineException.class)
	public void testNotTeacherDiscipline() throws SQLException, NotTeacherDisciplineException, IOException {
		lessonJdbcDao.add(new Lesson(1, new Discipline(2, "Mathematics"), new Audience(1, 111),
				new Teacher(1, "Tamara", "Nazarova"), new Group(1, "S111"),
				new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30))));
	}
}
