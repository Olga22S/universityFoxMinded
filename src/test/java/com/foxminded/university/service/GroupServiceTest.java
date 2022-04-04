package com.foxminded.university.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.foxminded.university.service.GroupServiceTest.TestData.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.foxminded.university.dao.jdbc.GroupJdbcDao;
import com.foxminded.university.entity.Group;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

	@Mock
	private GroupJdbcDao groupJdbcDao;

	@InjectMocks
	private GroupService groupService;

	@Test
	public void givenGroupId_whenGetById_thenGroupReturned() throws SQLException, IOException {
		Group expected = group;
		when(groupJdbcDao.getById(1)).thenReturn(expected);

		Group actual = groupService.getById(1);

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroup_whenUpdate_thenGroupUpdated() throws SQLException, IOException {
		groupService.update(group);
		verify(groupJdbcDao).update(group);
	}

	@Test
	public void givenGroup_whenAdd_thenGroupAdded() throws SQLException, IOException {
		groupService.add(group2);
		verify(groupJdbcDao).add(group2);
	}

	@Test
	public void givenGroup_whenRemove_thenGroupRemoved() throws SQLException, IOException {
		groupService.remove(group);
		verify(groupJdbcDao).remove(group);
	}

	@Test
	public void whenGetGroups_thenGroupsReturned() throws SQLException, IOException {
		List<Group> expected = new ArrayList<>();
		expected.add(group);
		expected.add(group2);
		when(groupJdbcDao.getAll()).thenReturn(expected);

		List<Group> actual = groupService.getGroups();

		assertEquals(expected, actual);
	}

	interface TestData {
		Group group = new Group(1, "s111");
		Group group2 = new Group(2, "s222");
	}
}
