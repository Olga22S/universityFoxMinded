package com.foxminded.university.dao.jdbc;

import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.dao.GenericDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.utils.ConnectionProvider;

public class TeacherJdbcDao implements GenericDao<Teacher> {

	private static final Logger logger = LoggerFactory.getLogger(TeacherJdbcDao.class);
	private static final String INSERT_IN_TEACHERS = "INSERT INTO teachers (teacher_id, first_name, last_name) VALUES(?,?,?)";
	private static final String INSERT_IN_TEACHERS_DISCIPLINES = "INSERT INTO teachers_disciplines (teacher_id, discipline_id) VALUES(?,?)";
	private static final String SELECT = "SELECT * FROM teachers WHERE teacher_id = ?";
	private static final String UPDATE = "UPDATE teachers SET first_name=?, last_name=? WHERE teacher_id=?";
	private static final String DELETE = "DELETE FROM teachers WHERE teacher_id=?";
	private static final String SELECT_ALL = "SELECT teacher_id, first_name, last_name FROM teachers";
	private ConnectionProvider connectionProvider;

	public TeacherJdbcDao(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	@Override
	public void add(Teacher teacher) {
		logger.debug("trying add new teacher={}", teacher);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_IN_TEACHERS)) {
			teacher.setId(NextIdGenerator.getNextId("teacher_sequence", connection));
			preparedStatement.setInt(1, teacher.getId());
			preparedStatement.setString(2, teacher.getFirstName());
			preparedStatement.setString(3, teacher.getLastName());
			preparedStatement.executeUpdate();
			logger.debug("Teacher was added");
		} catch (SQLException e) {
			throw new DaoException("Error when creating a teacher", e);
		}
	}

	public void addDisciplinesForTeacher(Teacher teacher, List<Discipline> disciplines) {
		logger.debug("trying add disciplines for teacher={}", teacher);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_IN_TEACHERS_DISCIPLINES)) {
			for (Discipline discipline : disciplines) {
				preparedStatement.setInt(1, teacher.getId());
				preparedStatement.setInt(2, discipline.getId());
				preparedStatement.executeUpdate();
			}
			logger.debug("Disciplines were added");
		} catch (SQLException e) {
			throw new DaoException("Error when adding disciplines for teacher", e);
		}
	}

	@Override
	public Teacher getById(int id) {
		logger.debug("trying get teacher by id={}", id);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return mapToTeacher(resultSet);
			}
			logger.debug("teacher was gotten by id={}", id);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a teacher by id", e);
		}
		return null;
	}

	@Override
	public void update(Teacher teacher) {
		logger.debug("trying update teacher={}", teacher);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setString(1, teacher.getFirstName());
			preparedStatement.setString(2, teacher.getLastName());
			preparedStatement.setInt(3, teacher.getId());
			preparedStatement.executeUpdate();
			logger.debug("teacher was updated");
		} catch (SQLException e) {
			throw new DaoException("Error when updating a teacher", e);
		}
	}

	@Override
	public void remove(Teacher teacher) {
		logger.debug("trying remove teacher={}", teacher);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
			preparedStatement.setInt(1, teacher.getId());
			preparedStatement.executeUpdate();
			logger.debug("teacher={} was removed", teacher);
		} catch (SQLException e) {
			throw new DaoException("Error when removing a teacher", e);
		}
	}

	@Override
	public List<Teacher> getAll() {
		logger.debug("trying get all teachers");
		List<Teacher> teachers = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
			while (resultSet.next()) {
				teachers.add(mapToTeacher(resultSet));
			}
			logger.debug("teachers were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting all teachers", e);
		}
		return teachers;
	}

	private Teacher mapToTeacher(ResultSet resultSet) throws SQLException {
		return new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("first_name"),
				resultSet.getString("last_name"));
	}
}
