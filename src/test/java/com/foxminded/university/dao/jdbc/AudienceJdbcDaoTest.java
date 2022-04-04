package com.foxminded.university.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;

import com.foxminded.university.dao.jdbc.AudienceJdbcDao;
import com.foxminded.university.entity.Audience;
import com.foxminded.university.utils.ConnectionProvider;
import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class AudienceJdbcDaoTest extends DBUnitConfig {

	private AudienceJdbcDao audienceJdbcDao;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		audienceJdbcDao = new AudienceJdbcDao(new ConnectionProvider());
		beforeData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("scheduleData.xml"));
		tester.setDataSet(beforeData);
		tester.onSetup();
	}

	public AudienceJdbcDaoTest(String name) {
		super(name);
	}

	@Test
	public void testGetAudienceById() {
		Audience expected = new Audience(1, 111);

		Audience result = audienceJdbcDao.getById(1);

		assertEquals(expected, result);
	}

	@Test
	public void testGetAllAudiences() {
		List<Audience> expected = new ArrayList<>();
		expected.add(new Audience(1, 111));
		expected.add(new Audience(2, 222));
		expected.add(new Audience(3, 333));
		expected.add(new Audience(4, 444));
		expected.add(new Audience(5, 555));

		List<Audience> actual = audienceJdbcDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void testSaveNewAudience() throws Exception {
		Audience audience = new Audience();
		audience.setNumber(666);
		audienceJdbcDao.add(audience);
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("audiences-data-save.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		String[] ignore = { "audience_id" };
		assertEqualsIgnoreCols(expectedData, actualData, "audiences", ignore);
	}

	@Test
	public void testRemoveAudience() throws Exception {
		audienceJdbcDao.remove(new Audience(5, 555));
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("audiences-data-remove.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("audiences"), actualData.getTable("audiences"));
	}
}
