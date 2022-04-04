package com.foxminded.university.dao.jdbc;

import java.time.*;
import java.util.*;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;

import com.foxminded.university.dao.jdbc.DayScheduleJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.utils.ConnectionProvider;
import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class DayScheduleJdbcDaoTest extends DBUnitConfig {

	private ConnectionProvider connectionProvider;
	private AudienceJdbcDao audienceJdbcDao;
	private DisciplineJdbcDao disciplineJdbcDao;
	private LessonTimeJdbcDao lessonTimeJdbcDao;
	private GroupJdbcDao groupJdbcDao;
	private TeacherJdbcDao teacherJdbcDao;
	private LessonJdbcDao lessonJdbcDao;
	private DayScheduleJdbcDao dayScheduleJdbcDao;

	public DayScheduleJdbcDaoTest(String name) {
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
		dayScheduleJdbcDao = new DayScheduleJdbcDao(connectionProvider, lessonJdbcDao);
		beforeData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("scheduleData.xml"));
		tester.setDataSet(beforeData);
		tester.onSetup();
	}

	@Test
	public void testGetDayScheduleByDate() {
		DaySchedule expected = new DaySchedule(1, LocalDate.of(2019, Month.SEPTEMBER, 2));
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(new Lesson(1, new Discipline(1, "Economy"), new Audience(1, 111), new Teacher(2, "Olga", "Petrova"),
				new Group(1, "S111"), new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30))));
		lessons.add(new Lesson(2, new Discipline(2, "Mathematics"), new Audience(2, 222),
				new Teacher(1, "Tamara", "Nazarova"), new Group(2, "S222"),
				new LessonTime(2, LocalTime.of(10, 30), LocalTime.of(12, 00))));
		expected.setLessons(lessons);

		DaySchedule result = dayScheduleJdbcDao.getByDate(LocalDate.of(2019, Month.SEPTEMBER, 2));

		assertEquals(expected, result);
	}

	@Test
	public void testGetAllDaySchedules() {
		List<DaySchedule> expected = getExpectedDaySchedules();

		List<DaySchedule> actual = dayScheduleJdbcDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void testSaveNewDaySchedule() throws Exception {
		DaySchedule daySchedule = new DaySchedule(LocalDate.of(2019, Month.SEPTEMBER, 17));
		dayScheduleJdbcDao.add(daySchedule);
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("daySchedules-data-save.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		String[] ignore = { "day_schedule_id" };
		assertEqualsIgnoreCols(expectedData, actualData, "day_schedules", ignore);
	}

	@Test
	public void testRemoveDaySchedule() throws Exception {
		DaySchedule daySchedule = new DaySchedule(3, LocalDate.of(2019, Month.SEPTEMBER, 16));
		dayScheduleJdbcDao.remove(daySchedule);
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("daySchedules-data-remove.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("day_schedules"), actualData.getTable("day_schedules"));
	}

	@Test
	public void testAddLessonInDaySchedule() throws Exception {
		DaySchedule daySchedule = new DaySchedule(3, LocalDate.of(2019, Month.SEPTEMBER, 16));
		Lesson lesson = new Lesson(1, new Discipline(1, "Economy"), new Audience(1, 111),
				new Teacher(2, "Olga", "Petrova"), new Group(1, "S111"),
				new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30)));
		dayScheduleJdbcDao.addLesson(lesson, daySchedule);
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("daySchedules-data-addLesson.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("day_schedules_lessons"),
				actualData.getTable("day_schedules_lessons"));
	}

	@Test
	public void testGetStudentDaySchedule() {
		DaySchedule expected = new DaySchedule(1, LocalDate.of(2019, Month.SEPTEMBER, 2));
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(new Lesson(1, new Discipline(1, "Economy"), new Audience(1, 111), new Teacher(2, "Olga", "Petrova"),
				new Group(1, "S111"), new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30))));
		expected.setLessons(lessons);

		DaySchedule result = dayScheduleJdbcDao.getStudentDaySchedule(new Student(1, "Oleg", "Ivanov", 1),
				LocalDate.of(2019, Month.SEPTEMBER, 2));

		assertEquals(expected, result);
	}

	@Test
	public void testGetTeacherDaySchedule() {
		DaySchedule expected = new DaySchedule(1, LocalDate.of(2019, Month.SEPTEMBER, 2));
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(new Lesson(2, new Discipline(2, "Mathematics"), new Audience(2, 222),
				new Teacher(1, "Tamara", "Nazarova"), new Group(2, "S222"),
				new LessonTime(2, LocalTime.of(10, 30), LocalTime.of(12, 00))));
		expected.setLessons(lessons);

		DaySchedule result = dayScheduleJdbcDao.getTeacherDaySchedule(new Teacher(1, "Tamara", "Nazarova"),
				LocalDate.of(2019, Month.SEPTEMBER, 2));

		assertEquals(expected, result);
	}

	private List<DaySchedule> getExpectedDaySchedules() {
		DaySchedule firstDaySchedule = new DaySchedule(1, LocalDate.of(2019, Month.SEPTEMBER, 2));
		List<Lesson> firstDayScheduleLessons = new ArrayList<>();
		firstDayScheduleLessons.add(
				new Lesson(1, new Discipline(1, "Economy"), new Audience(1, 111), new Teacher(2, "Olga", "Petrova"),
						new Group(1, "S111"), new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30))));
		firstDayScheduleLessons.add(new Lesson(2, new Discipline(2, "Mathematics"), new Audience(2, 222),
				new Teacher(1, "Tamara", "Nazarova"), new Group(2, "S222"),
				new LessonTime(2, LocalTime.of(10, 30), LocalTime.of(12, 00))));
		firstDaySchedule.setLessons(firstDayScheduleLessons);
		DaySchedule secondDaySchedule = new DaySchedule(2, LocalDate.of(2019, Month.SEPTEMBER, 9));
		List<Lesson> secondDayScheduleLessons = new ArrayList<>();
		secondDayScheduleLessons.add(
				new Lesson(3, new Discipline(3, "Informatics"), new Audience(2, 222), new Teacher(3, "Sergey", "Koval"),
						new Group(2, "S222"), new LessonTime(3, LocalTime.of(12, 30), LocalTime.of(14, 00))));
		secondDayScheduleLessons.add(
				new Lesson(4, new Discipline(1, "Economy"), new Audience(1, 111), new Teacher(3, "Sergey", "Koval"),
						new Group(1, "S111"), new LessonTime(2, LocalTime.of(10, 30), LocalTime.of(12, 00))));
		secondDaySchedule.setLessons(secondDayScheduleLessons);
		DaySchedule thirdDaySchedule = new DaySchedule(3, LocalDate.of(2019, Month.SEPTEMBER, 16));
		List<Lesson> thirdDayScheduleLessons = new ArrayList<>();
		thirdDaySchedule.setLessons(thirdDayScheduleLessons);
		List<DaySchedule> expected = new ArrayList<>();
		expected.add(firstDaySchedule);
		expected.add(secondDaySchedule);
		expected.add(thirdDaySchedule);
		return expected;
	}
}
