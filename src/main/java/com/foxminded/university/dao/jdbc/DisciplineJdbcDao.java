package com.foxminded.university.dao.jdbc;

import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.dao.DisciplineDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.utils.ConnectionProvider;

public class DisciplineJdbcDao implements DisciplineDao {

	private static final Logger logger = LoggerFactory.getLogger(DisciplineJdbcDao.class);
	private static final String INSERT = "INSERT INTO disciplines (discipline_id, name) VALUES(?,?)";
	private static final String SELECT = "SELECT * FROM disciplines WHERE discipline_id = ?";
	private static final String UPDATE = "UPDATE disciplines SET name=? WHERE discipline_id=?";
	private static final String DELETE = "DELETE FROM disciplines WHERE discipline_id=?";
	private static final String SELECT_ALL = "SELECT discipline_id, name FROM disciplines";
	private static final String SELECT_TEACHER_DISCIPLINES = "SELECT discipline_id, name FROM teachers_disciplines JOIN disciplines USING (discipline_id) WHERE teacher_id=?";
	private ConnectionProvider connectionProvider;

	public DisciplineJdbcDao(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	@Override
	public void add(Discipline discipline) {
		logger.debug("trying add new discipline={}", discipline);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
			discipline.setId(NextIdGenerator.getNextId("discipline_sequence", connection));
			preparedStatement.setInt(1, discipline.getId());
			preparedStatement.setString(2, discipline.getName());
			preparedStatement.executeUpdate();
			logger.debug("discipline was added");
		} catch (SQLException e) {
			throw new DaoException("Error when creating discipline", e);
		}
	}

	@Override
	public Discipline getById(int id) {
		logger.debug("trying get discipline by id={}", id);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				logger.debug("discipline was gotten by id={}", id);
				return mapToDiscipline(resultSet);
			}
		} catch (SQLException e) {
			throw new DaoException("Error when getting discipline by id", e);
		}
		return null;
	}

	@Override
	public void update(Discipline discipline) {
		logger.debug("trying update discipline={}", discipline);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setString(1, discipline.getName());
			preparedStatement.setInt(2, discipline.getId());
			preparedStatement.executeUpdate();
			logger.debug("discipline was updated");
		} catch (SQLException e) {
			throw new DaoException("Error when updating discipline", e);
		}
	}

	@Override
	public void remove(Discipline discipline) {
		logger.debug("trying remove discipline={}", discipline);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
			preparedStatement.setInt(1, discipline.getId());
			preparedStatement.executeUpdate();
			logger.debug("discipline={} was removed", discipline);
		} catch (SQLException e) {
			throw new DaoException("Error when removing discipline", e);
		}
	}

	@Override
	public List<Discipline> getAll() {
		logger.debug("trying get all disciplines");
		List<Discipline> disciplines = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
			while (resultSet.next()) {
				disciplines.add(mapToDiscipline(resultSet));
			}
			logger.debug("disciplines were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting all disciplines", e);
		}
		return disciplines;
	}

	@Override
	public List<Discipline> getTeacherDisciplines(Teacher teacher) {
		logger.debug("trying get disciplines for teacher={}", teacher);
		List<Discipline> disciplines = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TEACHER_DISCIPLINES)) {
			preparedStatement.setInt(1, teacher.getId());
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				disciplines.add(mapToDiscipline(resultSet));
			}
			logger.debug("disciplines for teacher={} were gotten", teacher);
		} catch (SQLException e) {
			throw new DaoException("Error when getting disciplines for teacher", e);
		}
		return disciplines;
	}

	private Discipline mapToDiscipline(ResultSet resultSet) throws SQLException {
		return new Discipline(resultSet.getInt("discipline_id"), resultSet.getString("name"));
	}
}
