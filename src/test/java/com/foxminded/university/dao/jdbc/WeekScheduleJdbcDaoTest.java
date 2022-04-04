package com.foxminded.university.dao.jdbc;

import java.time.*;
import java.util.*;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;

import com.foxminded.university.dao.jdbc.WeekScheduleJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.utils.ConnectionProvider;
import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class WeekScheduleJdbcDaoTest extends DBUnitConfig {

	private ConnectionProvider connectionProvider;
	private AudienceJdbcDao audienceJdbcDao;
	private DisciplineJdbcDao disciplineJdbcDao;
	private LessonTimeJdbcDao lessonTimeJdbcDao;
	private GroupJdbcDao groupJdbcDao;
	private TeacherJdbcDao teacherJdbcDao;
	private LessonJdbcDao lessonJdbcDao;
	private DayScheduleJdbcDao dayScheduleJdbcDao;
	private WeekScheduleJdbcDao weekScheduleJdbcDao;

	public WeekScheduleJdbcDaoTest(String name) {
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
		weekScheduleJdbcDao = new WeekScheduleJdbcDao(connectionProvider, dayScheduleJdbcDao);
		beforeData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("scheduleData.xml"));
		tester.setDataSet(beforeData);
		tester.onSetup();
	}

	@Test
	public void testGetWeekScheduleById() throws Exception {
		List<DaySchedule> daySchedules = new ArrayList<>();
		DaySchedule daySchedule = new DaySchedule(1, LocalDate.of(2019, Month.SEPTEMBER, 2));
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(new Lesson(1, new Discipline(1, "Economy"), new Audience(1, 111), new Teacher(2, "Olga", "Petrova"),
				new Group(1, "S111"), new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30))));
		lessons.add(new Lesson(2, new Discipline(2, "Mathematics"), new Audience(2, 222),
				new Teacher(1, "Tamara", "Nazarova"), new Group(2, "S222"),
				new LessonTime(2, LocalTime.of(10, 30), LocalTime.of(12, 00))));
		daySchedule.setLessons(lessons);
		daySchedules.add(daySchedule);
		WeekSchedule expected = new WeekSchedule(1, 1, daySchedules);

		WeekSchedule result = weekScheduleJdbcDao.getById(1);

		assertEquals(expected, result);
	}

	@Test
	public void testGetAllWeekSchedules() throws Exception {
		List<WeekSchedule> expected = getExpectedWeekSchedules();

		List<WeekSchedule> actual = weekScheduleJdbcDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void testSaveNewWeekSchedule() throws Exception {
		WeekSchedule weekSchedule = new WeekSchedule(3);
		weekScheduleJdbcDao.add(weekSchedule);
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("weekSchedule-data-save.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		String[] ignore = { "week_id" };
		assertEqualsIgnoreCols(expectedData, actualData, "week_schedules", ignore);
	}

	@Test
	public void testRemoveWeekSchedule() throws Exception {
		weekScheduleJdbcDao.remove(weekScheduleJdbcDao.getById(2));
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("weekSchedule-data-remove.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("week_schedules"), actualData.getTable("week_schedules"));
	}

	private List<WeekSchedule> getExpectedWeekSchedules() {
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
		List<DaySchedule> firstWeekScheduleDays = new ArrayList<>();
		firstWeekScheduleDays.add(firstDaySchedule);
		List<DaySchedule> secondWeekScheduleDays = new ArrayList<>();
		secondWeekScheduleDays.add(secondDaySchedule);
		WeekSchedule firstWeekSchedule = new WeekSchedule(1, 1, firstWeekScheduleDays);
		WeekSchedule secondWeekSchedule = new WeekSchedule(2, 2, secondWeekScheduleDays);
		List<WeekSchedule> expected = new ArrayList<>();
		expected.add(firstWeekSchedule);
		expected.add(secondWeekSchedule);
		return expected;
	}
}
