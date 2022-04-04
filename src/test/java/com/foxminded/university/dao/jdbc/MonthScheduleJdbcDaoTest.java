package com.foxminded.university.dao.jdbc;

import java.time.*;
import java.util.*;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;

import com.foxminded.university.dao.jdbc.MonthScheduleJdbcDao;
import com.foxminded.university.dao.jdbc.WeekScheduleJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.utils.ConnectionProvider;
import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class MonthScheduleJdbcDaoTest extends DBUnitConfig {

	private ConnectionProvider connectionProvider;
	private AudienceJdbcDao audienceJdbcDao;
	private DisciplineJdbcDao disciplineJdbcDao;
	private LessonTimeJdbcDao lessonTimeJdbcDao;
	private GroupJdbcDao groupJdbcDao;
	private TeacherJdbcDao teacherJdbcDao;
	private LessonJdbcDao lessonJdbcDao;
	private DayScheduleJdbcDao dayScheduleJdbcDao;
	private WeekScheduleJdbcDao weekScheduleJdbcDao;
	private MonthScheduleJdbcDao monthScheduleJdbcDao;

	public MonthScheduleJdbcDaoTest(String name) {
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
		monthScheduleJdbcDao = new MonthScheduleJdbcDao(connectionProvider, weekScheduleJdbcDao, dayScheduleJdbcDao);
		beforeData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("scheduleData.xml"));
		tester.setDataSet(beforeData);
		tester.onSetup();
	}

	@Test
	public void testGetMonthScheduleByIt() throws Exception {
		MonthSchedule expected = new MonthSchedule(1, "September", weekScheduleJdbcDao.getAll());

		MonthSchedule result = monthScheduleJdbcDao.getByMonth("September");

		assertEquals(expected, result);
	}

	@Test
	public void testGetAllMonthSchedules() throws Exception {
		List<MonthSchedule> expected = getExpectedMonthSchedules();

		List<MonthSchedule> actual = monthScheduleJdbcDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void testSaveNewMonthSchedule() throws Exception {
		MonthSchedule monthSchedule = new MonthSchedule("November", weekScheduleJdbcDao.getAll());
		monthScheduleJdbcDao.add(monthSchedule);
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("monthSchedule-data-save.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		String[] ignore = { "month_id" };
		assertEqualsIgnoreCols(expectedData, actualData, "month_schedules", ignore);
	}

	@Test
	public void testRemoveMonthSchedule() throws Exception {
		monthScheduleJdbcDao.remove(new MonthSchedule(2, "October", weekScheduleJdbcDao.getAll()));
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("monthSchedule-data-remove.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("month_schedules"), actualData.getTable("month_schedules"));
	}

	@Test
	public void testGetStudentMonthSchedule() throws Exception {
		MonthSchedule expected = getStudentMonthSchedule();

		MonthSchedule result = monthScheduleJdbcDao.getStudentMonthSchedule(new Student(1, "Oleg", "Ivanov", 1),
				"September");

		assertEquals(expected, result);
	}

	@Test
	public void testGetTeacherMonthSchedule() throws Exception {
		MonthSchedule expected = getTeacherMonthSchedule();

		MonthSchedule result = monthScheduleJdbcDao.getTeacherMonthSchedule(new Teacher(2, "Olga", "Petrova"),
				"September");

		assertEquals(expected, result);
	}

	private List<MonthSchedule> getExpectedMonthSchedules() {
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
		List<WeekSchedule> septemberWeekSchedules = new ArrayList<>();
		septemberWeekSchedules.add(firstWeekSchedule);
		septemberWeekSchedules.add(secondWeekSchedule);
		List<WeekSchedule> octoberWeekSchedules = new ArrayList<>();
		octoberWeekSchedules.add(firstWeekSchedule);
		octoberWeekSchedules.add(secondWeekSchedule);
		MonthSchedule september = new MonthSchedule(1, "September", septemberWeekSchedules);
		MonthSchedule october = new MonthSchedule(2, "October", octoberWeekSchedules);
		List<MonthSchedule> expected = new ArrayList<>();
		expected.add(september);
		expected.add(october);
		return expected;
	}

	private MonthSchedule getStudentMonthSchedule() {
		List<WeekSchedule> weekSchedules = new ArrayList<>();
		List<DaySchedule> daySchedules1 = new ArrayList<>();
		DaySchedule daySchedule1 = new DaySchedule(1, LocalDate.of(2019, Month.SEPTEMBER, 2));
		List<Lesson> lessons1 = new ArrayList<>();
		lessons1.add(
				new Lesson(1, new Discipline(1, "Economy"), new Audience(1, 111), new Teacher(2, "Olga", "Petrova"),
						new Group(1, "S111"), new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30))));
		daySchedule1.setLessons(lessons1);
		daySchedules1.add(daySchedule1);
		WeekSchedule weekSchedule1 = new WeekSchedule(1, 1, daySchedules1);
		List<DaySchedule> daySchedules2 = new ArrayList<>();
		DaySchedule daySchedule2 = new DaySchedule(2, LocalDate.of(2019, Month.SEPTEMBER, 9));
		List<Lesson> lessons2 = new ArrayList<>();
		lessons2.add(
				new Lesson(4, new Discipline(1, "Economy"), new Audience(1, 111), new Teacher(3, "Sergey", "Koval"),
						new Group(1, "S111"), new LessonTime(2, LocalTime.of(10, 30), LocalTime.of(12, 30))));
		daySchedule2.setLessons(lessons2);
		daySchedules2.add(daySchedule2);
		WeekSchedule weekSchedule2 = new WeekSchedule(2, 2, daySchedules2);
		weekSchedules.add(weekSchedule1);
		weekSchedules.add(weekSchedule2);
		return new MonthSchedule(1, "September", weekSchedules);
	}

	private MonthSchedule getTeacherMonthSchedule() {
		List<WeekSchedule> weekSchedules = new ArrayList<>();
		List<DaySchedule> daySchedules = new ArrayList<>();
		DaySchedule daySchedule = new DaySchedule(1, LocalDate.of(2019, Month.SEPTEMBER, 2));
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(new Lesson(1, new Discipline(1, "Economy"), new Audience(1, 111), new Teacher(2, "Olga", "Petrova"),
				new Group(1, "S111"), new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30))));
		daySchedule.setLessons(lessons);
		daySchedules.add(daySchedule);
		WeekSchedule weekSchedule = new WeekSchedule(1, 1, daySchedules);
		weekSchedules.add(weekSchedule);
		return new MonthSchedule(1, "September", weekSchedules);
	}
}
