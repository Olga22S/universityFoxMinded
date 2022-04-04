package com.foxminded.university.dao.jdbc;

import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.dao.GenericDao;
import com.foxminded.university.entity.LessonTime;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.utils.ConnectionProvider;

public class LessonTimeJdbcDao implements GenericDao<LessonTime> {

	private static final Logger logger = LoggerFactory.getLogger(LessonTimeJdbcDao.class);
	private static final String INSERT = "INSERT INTO lesson_times (lesson_time_id, start_time, end_time) VALUES(?,?,?)";
	private static final String SELECT = "SELECT * FROM lesson_times WHERE lesson_time_id = ?";
	private static final String UPDATE = "UPDATE lesson_times SET start_time=?, end_time=? WHERE lesson_time_id=?";
	private static final String DELETE = "DELETE FROM lesson_times WHERE lesson_time_id=?";
	private static final String SELECT_ALL = "SELECT lesson_time_id, start_time, end_time FROM lesson_times";
	private ConnectionProvider connectionProvider;

	public LessonTimeJdbcDao(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	@Override
	public void add(LessonTime lessonTime) {
		logger.debug("trying add new lessonTime={}", lessonTime);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
			lessonTime.setId(NextIdGenerator.getNextId("audience_sequence", connection));
			preparedStatement.setInt(1, lessonTime.getId());
			preparedStatement.setTime(2, Time.valueOf(lessonTime.getStartTime()));
			preparedStatement.setTime(3, Time.valueOf(lessonTime.getEndTime()));
			preparedStatement.executeUpdate();
			logger.debug("lessonTime was added");
		} catch (SQLException e) {
			throw new DaoException("Error when creating a lessonTime", e);
		}
	}

	@Override
	public LessonTime getById(int id) {
		logger.debug("trying get lessonTime by id={}", id);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				logger.debug("lessonTime was gotten by id={}", id);
				return mapToLessonTime(resultSet);
			}
		} catch (SQLException e) {
			throw new DaoException("Error when getting a lessonTime by id", e);
		}
		return null;
	}

	@Override
	public void update(LessonTime lessonTime) {
		logger.debug("trying update lessonTime={}", lessonTime);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setTime(1, Time.valueOf(lessonTime.getStartTime()));
			preparedStatement.setTime(2, Time.valueOf(lessonTime.getEndTime()));
			preparedStatement.setInt(3, lessonTime.getId());
			preparedStatement.executeUpdate();
			logger.debug("lessonTime was updated");
		} catch (SQLException e) {
			throw new DaoException("Error when updating a lessonTime", e);
		}
	}

	@Override
	public void remove(LessonTime lessonTime) {
		logger.debug("trying remove lessonTime={}", lessonTime);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
			preparedStatement.setInt(1, lessonTime.getId());
			preparedStatement.executeUpdate();
			logger.debug("lessonTime={} was removed", lessonTime);
		} catch (SQLException e) {
			throw new DaoException("Error when removing a lessonTime", e);
		}
	}

	@Override
	public List<LessonTime> getAll() {
		logger.debug("trying get all lessonTimes");
		List<LessonTime> lessonTimes = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
			while (resultSet.next()) {
				lessonTimes.add(mapToLessonTime(resultSet));
			}
			logger.debug("lessonTimes were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting all lessonTimes", e);
		}
		return lessonTimes;
	}

	private LessonTime mapToLessonTime(ResultSet resultSet) throws SQLException {
		return new LessonTime(resultSet.getInt("lesson_time_id"), resultSet.getTime("start_time").toLocalTime(),
				resultSet.getTime("end_time").toLocalTime());
	}
}
