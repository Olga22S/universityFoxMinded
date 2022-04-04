package com.foxminded.university.dao.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.dao.ScheduleDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.utils.ConnectionProvider;

public class ScheduleJdbcDao implements ScheduleDao {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleJdbcDao.class);
	private static final String INSERT_IN_SCHEDULES = "INSERT INTO schedules (schedule_id, year) VALUES(?,?)";
	private static final String INSERT_IN_SCHEDULES_MONTH_SCHEDULES = "INSERT INTO schedules_month_schedules (schedule_id, month_id) VALUES(?,?)";
	private static final String SELECT_MONTHS_FROM_SCHEDULE_BY_YEAR = "SELECT month_id FROM schedules_month_schedules  "
			+ "JOIN schedules USING (schedule_id) WHERE year = ?";
	private static final String SELECT_MONTHS_FROM_SCHEDULE_BY_ID = "SELECT month_id FROM schedules_month_schedules WHERE schedule_id = ?";
	private static final String UPDATE = "UPDATE schedules SET year=? WHERE schedule_id=?";
	private static final String DELETE = "DELETE FROM schedules WHERE schedule_id=?";
	private static final String SELECT_ALL = "SELECT schedule_id FROM schedules";
	private static final String SELECT_SCHEDULE_BY_YEAR = "SELECT * FROM schedules WHERE year=?";
	private static final String SELECT_SCHEDULE_BY_ID = "SELECT * FROM schedules WHERE schedule_id=?";
	private ConnectionProvider connectionProvider;
	private MonthScheduleJdbcDao monthScheduleJdbcDao;

	public ScheduleJdbcDao(ConnectionProvider connectionProvider, MonthScheduleJdbcDao monthScheduleJdbcDao) {
		this.connectionProvider = connectionProvider;
		this.monthScheduleJdbcDao = monthScheduleJdbcDao;
	}

	@Override
	public void add(Schedule schedule) {
		logger.debug("trying add new schedule={}", schedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_IN_SCHEDULES)) {
			schedule.setId(NextIdGenerator.getNextId("schedule_sequence", connection));
			preparedStatement.setInt(1, schedule.getId());
			preparedStatement.setInt(2, schedule.getYear());
			preparedStatement.executeUpdate();
			logger.debug("schedule was added");
		} catch (SQLException e) {
			throw new DaoException("Error when creating a schedule", e);
		}
	}

	public void addMonthsInSchedule(Schedule schedule, List<MonthSchedule> monthSchedules) {
		logger.debug("trying add month schedules in schedule");
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(INSERT_IN_SCHEDULES_MONTH_SCHEDULES)) {
			for (MonthSchedule monthSchedule : monthSchedules) {
				preparedStatement.setInt(1, schedule.getId());
				preparedStatement.setInt(2, monthSchedule.getId());
				preparedStatement.executeUpdate();
			}
			logger.debug("month schedules were added in schedule");
		} catch (SQLException e) {
			throw new DaoException("Error when adding a month schedules in schedule", e);
		}
	}

	@Override
	public Schedule getByYear(int year) {
		logger.debug("trying get schedule by year={}", year);
		Schedule schedule = Optional.of(getScheduleByYear(year))
				.orElseThrow(() -> new EntityNotFoundException("Schedule by year=" + year + " is not found!"));
		List<MonthSchedule> monthSchedulesForYear = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(SELECT_MONTHS_FROM_SCHEDULE_BY_YEAR)) {
			preparedStatement.setInt(1, year);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				monthSchedulesForYear.add(monthScheduleJdbcDao.getById(resultSet.getInt("month_id")));
			}
			logger.debug("schedule by year={} was gotten", year);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a schedule by year", e);
		}
		schedule.setMonthSchedules(monthSchedulesForYear);
		return schedule;
	}

	@Override
	public Schedule getById(int id) {
		logger.debug("trying get schedule by id={}", id);
		Schedule schedule = Optional.of(getScheduleById(id))
				.orElseThrow(() -> new EntityNotFoundException("Schedule by id=" + id + " is not found!"));
		List<MonthSchedule> monthSchedulesForYear = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MONTHS_FROM_SCHEDULE_BY_ID)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				monthSchedulesForYear.add(monthScheduleJdbcDao.getById(resultSet.getInt("month_id")));
			}
			logger.debug("schedule was gotten by id={}", id);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a schedule by id", e);
		}
		schedule.setMonthSchedules(monthSchedulesForYear);
		return schedule;
	}

	@Override
	public void update(Schedule schedule) {
		logger.debug("trying update schedule={}", schedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setInt(1, schedule.getYear());
			preparedStatement.setInt(2, schedule.getId());
			preparedStatement.executeUpdate();
			logger.debug("schedule was updated");
		} catch (SQLException e) {
			throw new DaoException("Error when updating a schedule", e);
		}
	}

	@Override
	public void remove(Schedule schedule) {
		logger.debug("trying remove schedule={}", schedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
			preparedStatement.setInt(1, schedule.getId());
			preparedStatement.executeUpdate();
			logger.debug("schedule was removed");
		} catch (SQLException e) {
			throw new DaoException("Error when removing a schedule", e);
		}
	}

	@Override
	public List<Schedule> getAll() {
		logger.debug("trying get all schedules");
		List<Schedule> schedules = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
			while (resultSet.next()) {
				schedules.add(getById(resultSet.getInt("schedule_id")));
			}
			logger.debug("schedules were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting all schedules", e);
		}
		return schedules;
	}

	private Schedule getScheduleById(int id) {
		logger.debug("trying get schedule by id={}", id);
		Schedule schedule = null;
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SCHEDULE_BY_ID)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				schedule = new Schedule();
				schedule.setId(resultSet.getInt("schedule_id"));
				schedule.setYear(resultSet.getInt("year"));
			}
			logger.debug("schedule was gotten by id={}", id);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a schedule by id", e);
		}
		return schedule;
	}

	private Schedule getScheduleByYear(int year) {
		logger.debug("trying get schedule by year={}", year);
		Schedule schedule = null;
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SCHEDULE_BY_YEAR)) {
			preparedStatement.setInt(1, year);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				schedule = new Schedule();
				schedule.setId(resultSet.getInt("schedule_id"));
				schedule.setYear(resultSet.getInt("year"));
			}
			logger.debug("schedule was gotten by year={}", year);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a schedule by year", e);
		}
		return schedule;
	}
}
