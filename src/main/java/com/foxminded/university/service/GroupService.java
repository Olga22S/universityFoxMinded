package com.foxminded.university.service;

import static java.util.Objects.isNull;
import java.util.*;
import com.foxminded.university.dao.jdbc.GroupJdbcDao;
import com.foxminded.university.entity.Group;
import com.foxminded.university.exception.EntityNotFoundException;

public class GroupService {

	private GroupJdbcDao groupJdbcDao;

	public GroupService(GroupJdbcDao groupJdbcDao) {
		this.groupJdbcDao = groupJdbcDao;
	}

	public void add(Group group) {
		groupJdbcDao.add(group);
	}

	public Group getById(int id) {
		Group group = groupJdbcDao.getById(id);
		if (isNull(group)) {
			throw new EntityNotFoundException("Group by id=" + id + " was not found");
		} else {
			return group;
		}
	}

	public void update(Group group) {
		groupJdbcDao.update(group);
	}

	public void remove(Group group) {
		groupJdbcDao.remove(group);
	}

	public List<Group> getGroups() {
		return groupJdbcDao.getAll();
	}
}
