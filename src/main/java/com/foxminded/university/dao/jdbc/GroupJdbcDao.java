package com.foxminded.university.dao.jdbc;

import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.dao.GenericDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.utils.ConnectionProvider;

public class GroupJdbcDao implements GenericDao<Group> {

	private static final Logger logger = LoggerFactory.getLogger(GroupJdbcDao.class);
	private static final String INSERT = "INSERT INTO groups (group_id, group_name) VALUES(?,?)";
	private static final String SELECT = "SELECT * FROM groups WHERE group_id = ?";
	private static final String UPDATE = "UPDATE groups SET group_name=? WHERE group_id=?";
	private static final String DELETE = "DELETE FROM groups WHERE group_id=?";
	private static final String SELECT_ALL = "SELECT group_id, group_name FROM groups";
	private ConnectionProvider connectionProvider;

	public GroupJdbcDao(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	@Override
	public void add(Group group) {
		logger.debug("trying add new group={}", group);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
			group.setId(NextIdGenerator.getNextId("group_sequence", connection));
			preparedStatement.setInt(1, group.getId());
			preparedStatement.setString(2, group.getName());
			preparedStatement.executeUpdate();
			logger.debug("group was added");
		} catch (SQLException e) {
			throw new DaoException("Error when creating a group", e);
		}
	}

	@Override
	public Group getById(int id) {
		logger.debug("trying get group by id={}", id);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				logger.debug("group was gotten by id={}", id);
				return mapToGroup(resultSet);
			}
		} catch (SQLException e) {
			throw new DaoException("Error when getting a group by id", e);
		}
		return null;
	}

	@Override
	public void update(Group group) {
		logger.debug("trying update group={}", group);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setString(1, group.getName());
			preparedStatement.setInt(2, group.getId());
			preparedStatement.executeUpdate();
			logger.debug("group was updated");
		} catch (SQLException e) {
			throw new DaoException("Error when updating a group", e);
		}
	}

	@Override
	public void remove(Group group) {
		logger.debug("trying remove group={}", group);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
			preparedStatement.setInt(1, group.getId());
			preparedStatement.executeUpdate();
			logger.debug("group={} was removed", group);
		} catch (SQLException e) {
			throw new DaoException("Error when removing a group", e);
		}
	}

	@Override
	public List<Group> getAll() {
		logger.debug("trying get all groups");
		List<Group> groups = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
			while (resultSet.next()) {
				groups.add(mapToGroup(resultSet));
			}
			logger.debug("groups were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting all groups", e);
		}
		return groups;
	}

	private Group mapToGroup(ResultSet resultSet) throws SQLException {
		return new Group(resultSet.getInt("group_id"), resultSet.getString("group_name"));
	}
}
