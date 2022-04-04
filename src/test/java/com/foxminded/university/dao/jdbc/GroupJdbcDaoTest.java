package com.foxminded.university.dao.jdbc;

import java.util.ArrayList;
import java.util.List;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;

import com.foxminded.university.dao.jdbc.GroupJdbcDao;
import com.foxminded.university.entity.Group;
import com.foxminded.university.utils.ConnectionProvider;
import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class GroupJdbcDaoTest extends DBUnitConfig {

	private GroupJdbcDao groupJdbcDao;

	public GroupJdbcDaoTest(String name) {
		super(name);
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		groupJdbcDao = new GroupJdbcDao(new ConnectionProvider());
		beforeData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("scheduleData.xml"));
		tester.setDataSet(beforeData);
		tester.onSetup();
	}

	@Test
	public void testGetGroupById() throws Exception {
		Group expected = new Group(1, "S111");

		Group result = groupJdbcDao.getById(1);

		assertEquals(expected, result);
	}

	@Test
	public void testGetAllGroups() throws Exception {
		List<Group> expected = new ArrayList<>();
		expected.add(new Group(1, "S111"));
		expected.add(new Group(2, "S222"));
		expected.add(new Group(3, "S333"));

		List<Group> actual = groupJdbcDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void testSaveNewGroup() throws Exception {
		Group group = new Group();
		group.setName("S444");
		groupJdbcDao.add(group);
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("groups-data-save.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		String[] ignore = { "group_id" };
		assertEqualsIgnoreCols(expectedData, actualData, "groups", ignore);
	}

	@Test
	public void testRemoveGroup() throws Exception {
		groupJdbcDao.remove(new Group(3, "S333"));
		IDataSet expectedData = new FlatXmlDataSetBuilder()
				.build(Thread.currentThread().getContextClassLoader().getResourceAsStream("groups-data-remove.xml"));

		IDataSet actualData = tester.getConnection().createDataSet();

		Assertion.assertEquals(expectedData.getTable("groups"), actualData.getTable("groups"));
	}
}
