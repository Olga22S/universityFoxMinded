package com.foxminded.university.dao.jdbc;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;

import com.foxminded.university.dao.jdbc.LessonTimeJdbcDao;
import com.foxminded.university.entity.LessonTime;
import com.foxminded.university.utils.ConnectionProvider;
import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class LessonTimeJdbcDaoTest extends DBUnitConfig {

	private LessonTimeJdbcDao lessonTimeJdbcDao;

	public LessonTimeJdbcDaoTest(String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		lessonTimeJdbcDao = new LessonTimeJdbcDao(new ConnectionProvider());
		beforeData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("scheduleData.xml"));
		tester.setDataSet(beforeData);
		tester.onSetup();
	}

	@Test
	public void testGetLessonTimeById() throws Exception {
		LessonTime expected = new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30));

		LessonTime result = lessonTimeJdbcDao.getById(1);

		assertEquals(expected, result);
	}

	@Test
	public void testGetAllLessonTimes() throws Exception {
		List<LessonTime> expected = new ArrayList<>();
		expected.add(new LessonTime(1, LocalTime.of(9, 00), LocalTime.of(10, 30)));
		expected.add(new LessonTime(2, LocalTime.of(10, 30), LocalTime.of(12, 00)));
		expected.add(new LessonTime(3, LocalTime.of(12, 30), LocalTime.of(14, 00)));
		expected.add(new LessonTime(4, LocalTime.of(14, 00), LocalTime.of(15, 30)));

		List<LessonTime> actual = lessonTimeJdbcDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void testSaveNewLessonTime() throws Exception {
		LessonTime lessonTime = new LessonTime();
		lessonTime.setStartTime(LocalTime.of(17, 00));
		lessonTime.setEndTime(LocalTime.of(19, 30));
		lessonTimeJdbcDao.add(lessonTime);
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("lessonTimes-data-save.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		String[] ignore = { "lesson_time_id" };
		assertEqualsIgnoreCols(expectedData, actualData, "lesson_times", ignore);
	}

	@Test
	public void testRemoveLessonTime() throws Exception {
		lessonTimeJdbcDao.remove(new LessonTime(4, LocalTime.of(14, 00), LocalTime.of(15, 30)));
		IDataSet expectedData = new FlatXmlDataSetBuilder().build(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("lessonTimes-data-remove.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("lesson_times"), actualData.getTable("lesson_times"));
	}
}
