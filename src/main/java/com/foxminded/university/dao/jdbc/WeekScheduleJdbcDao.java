package com.foxminded.university.dao.jdbc;

import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.dao.GenericDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.utils.ConnectionProvider;

public class WeekScheduleJdbcDao implements GenericDao<WeekSchedule> {

	private static final Logger logger = LoggerFactory.getLogger(WeekScheduleJdbcDao.class);
	private static final String INSERT_IN_WEEK_SCHEDULES = "INSERT INTO week_schedules (week_id, week_number) VALUES(?,?)";
	private static final String INSERT_IN_WEEK_SCHEDULES_DAY_SCHEDULES = "INSERT INTO week_schedules_day_schedules (week_id, day_schedule_id) VALUES(?,?)";
	private static final String SELECT_WEEK = "SELECT * FROM week_schedules WHERE week_id = ?";
	private static final String SELECT_DAY_SCHEDULES = "SELECT day_schedule_id FROM week_schedules_day_schedules WHERE week_id=?";
	private static final String UPDATE = "UPDATE week_schedules SET week_number=? WHERE week_id=?";
	private static final String DELETE = "DELETE FROM week_schedules WHERE week_id=?";
	private static final String SELECT_ALL = "SELECT week_id FROM week_schedules";
	private ConnectionProvider connectionProvider;
	private DayScheduleJdbcDao dayScheduleJdbcDao;

	public WeekScheduleJdbcDao(ConnectionProvider connectionProvider, DayScheduleJdbcDao dayScheduleJdbcDao) {
		this.connectionProvider = connectionProvider;
		this.dayScheduleJdbcDao = dayScheduleJdbcDao;
	}

	@Override
	public void add(WeekSchedule weekSchedule) {
		logger.debug("trying add new weekSchedule={}", weekSchedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_IN_WEEK_SCHEDULES)) {
			weekSchedule.setId(NextIdGenerator.getNextId("week_schedule_sequence", connection));
			preparedStatement.setInt(1, weekSchedule.getId());
			preparedStatement.setInt(2, weekSchedule.getWeekNumber());
			preparedStatement.executeUpdate();
			logger.debug("weekSchedule was added");
		} catch (SQLException e) {
			throw new DaoException("Error when creating a weekSchedule", e);
		}
	}

	public void addDaySchedulesInWeek(WeekSchedule weekSchedule, List<DaySchedule> daySchedules) {
		logger.debug("trying add daySchedules in weekSchedule");
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(INSERT_IN_WEEK_SCHEDULES_DAY_SCHEDULES);) {
			for (DaySchedule daySchedule : daySchedules) {
				preparedStatement.setInt(1, weekSchedule.getId());
				preparedStatement.setInt(2, daySchedule.getId());
				preparedStatement.executeUpdate();
			}
			logger.debug("daySchedules were added in weekSchedule");
		} catch (SQLException e) {
			throw new DaoException("Error when adding the daySchedules in weekSchedule", e);
		}
	}

	@Override
	public WeekSchedule getById(int id) {
		logger.debug("trying get weekSchedule by id={}", id);
		WeekSchedule weekSchedule = Optional.of(getWeekById(id))
				.orElseThrow(() -> new EntityNotFoundException("WeekSchedule by id=" + id + " is not found!"));
		List<DaySchedule> daySchedulesForWeek = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DAY_SCHEDULES)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				daySchedulesForWeek.add(dayScheduleJdbcDao.getById(resultSet.getInt("day_schedule_id")));
			}
			logger.debug("weekSchedule was gotten by id={}", id);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a weekSchedule by id", e);
		}
		weekSchedule.setDaySchedules(daySchedulesForWeek);
		return weekSchedule;
	}

	@Override
	public void update(WeekSchedule weekSchedule) {
		logger.debug("trying update weekSchedule={}", weekSchedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setInt(1, weekSchedule.getWeekNumber());
			preparedStatement.setInt(2, weekSchedule.getId());
			preparedStatement.executeUpdate();
			logger.debug("weekSchedule was updated");
		} catch (SQLException e) {
			throw new DaoException("Error when updating a weekSchedule", e);
		}
	}

	@Override
	public void remove(WeekSchedule weekSchedule) {
		logger.debug("trying remove weekSchedule={}", weekSchedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
			preparedStatement.setInt(1, weekSchedule.getId());
			preparedStatement.executeUpdate();
			logger.debug("weekSchedule={} was removed", weekSchedule);
		} catch (SQLException e) {
			throw new DaoException("Error when removing a weekSchedule", e);
		}
	}

	@Override
	public List<WeekSchedule> getAll() {
		logger.debug("trying get all weekSchedules");
		List<WeekSchedule> weekSchedules = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
			while (resultSet.next()) {
				weekSchedules.add(getById(resultSet.getInt("week_id")));
			}
			logger.debug("weekSchedules were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting all weekSchedules", e);
		}
		return weekSchedules;
	}

	private WeekSchedule getWeekById(int id) {
		WeekSchedule weekSchedule = null;
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_WEEK)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				weekSchedule = new WeekSchedule();
				weekSchedule.setId(resultSet.getInt("week_id"));
				weekSchedule.setWeekNumber(resultSet.getInt("week_number"));
			}
		} catch (SQLException e) {
			throw new DaoException("Error when getting a weekSchedule by id", e);
		}
		return weekSchedule;
	}
}
