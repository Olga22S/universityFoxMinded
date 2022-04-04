package com.foxminded.university.dao.jdbc;

import java.time.*;
import java.util.*;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;

import com.foxminded.university.dao.jdbc.MonthScheduleJdbcDao;
import com.foxminded.university.dao.jdbc.ScheduleJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.utils.ConnectionProvider;
import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class ScheduleJdbcDaoTest extends DBUnitConfig {

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
	private ScheduleJdbcDao scheduleJdbcDao;

	public ScheduleJdbcDaoTest(String name) {
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
		scheduleJdbcDao = new ScheduleJdbcDao(connectionProvider, monthScheduleJdbcDao);
		beforeData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("scheduleData.xml"));
		tester.setDataSet(beforeData);
		tester.onSetup();
	}

	@Test
	public void testGetScheduleByYear() throws Exception {
		Schedule expected = new Schedule(1, 2019, monthScheduleJdbcDao.getAll());

		Schedule result = scheduleJdbcDao.getByYear(2019);

		assertEquals(expected, result);
	}

	@Test
	public void testGetAllSchedules() throws Exception {
		List<Schedule> expected = getExpectedSchedules();

		List<Schedule> actual = scheduleJdbcDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void testSaveNewSchedule() throws Exception {
		Schedule schedule = new Schedule(2021);
		scheduleJdbcDao.add(schedule);
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("schedules-data-save.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		String[] ignore = { "schedule_id" };
		assertEqualsIgnoreCols(expectedData, actualData, "schedules", ignore);
	}

	@Test
	public void testRemoveSchedule() throws Exception {
		scheduleJdbcDao.remove(new Schedule(1, 2019, monthScheduleJdbcDao.getAll()));
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("schedule-data-remove.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("schedules"), actualData.getTable("schedules"));
	}

	@Test
	public void testAddMonthsInSchedule() throws Exception {
		scheduleJdbcDao.addMonthsInSchedule(scheduleJdbcDao.getByYear(2020), monthScheduleJdbcDao.getAll());
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("schedule-addMonthsInSchedule.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("schedules_month_schedules"),
				actualData.getTable("schedules_month_schedules"));
	}

	private List<Schedule> getExpectedSchedules() {
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
		List<MonthSchedule> firstScheduleMonths = new ArrayList<>();
		firstScheduleMonths.add(september);
		firstScheduleMonths.add(october);
		List<MonthSchedule> secondScheduleMonths = new ArrayList<>();
		Schedule firstSchedule = new Schedule(1, 2019, firstScheduleMonths);
		Schedule secondSchedule = new Schedule(2, 2020, secondScheduleMonths);
		List<Schedule> expected = new ArrayList<>();
		expected.add(firstSchedule);
		expected.add(secondSchedule);
		return expected;
	}
}
