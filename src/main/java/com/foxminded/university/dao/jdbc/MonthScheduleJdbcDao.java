package com.foxminded.university.dao.jdbc;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.dao.MonthScheduleDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.utils.*;

public class MonthScheduleJdbcDao implements MonthScheduleDao {

	private static final Logger logger = LoggerFactory.getLogger(MonthScheduleJdbcDao.class);
	private static final String INSERT_IN_MONTH_SCHEDULES = "INSERT INTO month_schedules (month_id, month_name) VALUES(?,?)";
	private static final String INSERT_IN_MONTH_SCHEDULES_WEEK_SCHEDULES = "INSERT INTO month_schedules_week_schedules (month_id, week_id) VALUES(?,?)";
	private static final String UPDATE = "UPDATE month_schedules SET month_name=? WHERE month_id=?";
	private static final String DELETE = "DELETE FROM month_schedules WHERE month_id=?";
	private static final String SELECT_ALL = "SELECT month_id from month_schedules";
	private static final String SELECT_WEEK_BY_MONTH_NAME = "SELECT week_id FROM month_schedules_week_schedules "
			+ "JOIN month_schedules USING (month_id) WHERE month_name = ?";
	private static final String SELECT_WEEK_BY_MONTH_ID = "SELECT week_id FROM month_schedules_week_schedules "
			+ "WHERE month_id = ?";
	private static final String SELECT_MONTH_BY_ID = "SELECT month_id, month_name FROM month_schedules WHERE month_id = ?";
	private static final String SELECT_MONTH_BY_NAME = "SELECT month_id, month_name FROM month_schedules WHERE month_name = ?";
	private static final String SELECT_TEACHER_DAYS = "SELECT date "
			+ "FROM day_schedules_lessons JOIN day_schedules USING (day_schedule_id) "
			+ "JOIN lessons USING (lesson_id) JOIN teachers USING (teacher_id) WHERE teacher_id=?";
	private static final String SELECT_STUDENT_DAYS = "SELECT date FROM day_schedules_lessons "
			+ "JOIN day_schedules USING (day_schedule_id) JOIN lessons USING (lesson_id) "
			+ "JOIN groups USING (group_id) JOIN students USING (group_id) WHERE student_id=?";
	private ConnectionProvider connectionProvider;
	private WeekScheduleJdbcDao weekScheduleJdbcDao;
	private DayScheduleJdbcDao dayScheduleJdbcDao;

	public MonthScheduleJdbcDao(ConnectionProvider connectionProvider, WeekScheduleJdbcDao weekScheduleJdbcDao,
			DayScheduleJdbcDao dayScheduleJdbcDao) {
		this.connectionProvider = connectionProvider;
		this.weekScheduleJdbcDao = weekScheduleJdbcDao;
		this.dayScheduleJdbcDao = dayScheduleJdbcDao;
	}

	@Override
	public void add(MonthSchedule monthSchedule) {
		logger.debug("trying add new monthSchedule={}", monthSchedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_IN_MONTH_SCHEDULES)) {
			monthSchedule.setId(NextIdGenerator.getNextId("month_schedule_sequence", connection));
			preparedStatement.setInt(1, monthSchedule.getId());
			preparedStatement.setString(2, monthSchedule.getMonth());
			preparedStatement.executeUpdate();
			logger.debug("monthSchedule was added");
		} catch (SQLException e) {
			throw new DaoException("Error when creating a monthSchedule", e);
		}
	}

	public void addWeekSchedulesInMonth(MonthSchedule monthSchedule, List<WeekSchedule> weekSchedules) {
		logger.debug("trying add weekSchedules in monthSchedule");
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(INSERT_IN_MONTH_SCHEDULES_WEEK_SCHEDULES)) {
			for (WeekSchedule weekSchedule : weekSchedules) {
				preparedStatement.setInt(1, monthSchedule.getId());
				preparedStatement.setInt(2, weekSchedule.getId());
				preparedStatement.executeUpdate();
			}
			logger.debug("weekSchedules were added in monthSchedule");
		} catch (SQLException e) {
			throw new DaoException("Error when adding weekSchedules in monthSchedule", e);
		}
	}

	@Override
	public MonthSchedule getByMonth(String month) {
		logger.debug("trying get monthSchedule by month={}", month);
		MonthSchedule monthSchedule = Optional.of(getMonthByName(month))
				.orElseThrow(() -> new EntityNotFoundException("MonthSchedule by month=" + month + " is not found!"));
		List<WeekSchedule> weekSchedules = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_WEEK_BY_MONTH_NAME)) {
			preparedStatement.setString(1, month);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				weekSchedules.add(weekScheduleJdbcDao.getById(resultSet.getInt("week_id")));
			}
			logger.debug("monthSchedule was gotten by month={}", month);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a monthSchedule by month", e);
		}
		monthSchedule.setWeekSchedules(weekSchedules);
		return monthSchedule;
	}

	@Override
	public MonthSchedule getById(int id) {
		logger.debug("trying get monthSchedule by id={}", id);
		MonthSchedule monthSchedule = Optional.of(getMonthById(id))
				.orElseThrow(() -> new EntityNotFoundException("MonthSchedule by id=" + id + " is not found!"));
		List<WeekSchedule> weekSchedules = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_WEEK_BY_MONTH_ID)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				weekSchedules.add(weekScheduleJdbcDao.getById(resultSet.getInt("week_id")));
			}
			logger.debug("monthSchedule was gotten by id={}", id);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a monthSchedule by id", e);
		}
		monthSchedule.setWeekSchedules(weekSchedules);
		return monthSchedule;
	}

	@Override
	public void update(MonthSchedule monthSchedule) {
		logger.debug("trying update monthSchedule={}", monthSchedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setString(1, monthSchedule.getMonth());
			preparedStatement.setInt(2, monthSchedule.getId());
			preparedStatement.executeUpdate();
			logger.debug("monthSchedule was updated");
		} catch (SQLException e) {
			throw new DaoException("Error when updating a monthSchedule", e);
		}
	}

	@Override
	public void remove(MonthSchedule monthSchedule) {
		logger.debug("trying remove monthSchedule={}", monthSchedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
			preparedStatement.setInt(1, monthSchedule.getId());
			preparedStatement.executeUpdate();
			logger.debug("monthSchedule={} was removed", monthSchedule);
		} catch (SQLException e) {
			throw new DaoException("Error when removing a monthSchedule", e);
		}
	}

	@Override
	public List<MonthSchedule> getAll() {
		logger.debug("trying get all monthSchedules");
		List<MonthSchedule> monthSchedules = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
			while (resultSet.next()) {
				monthSchedules.add(getById(resultSet.getInt("month_id")));
			}
			logger.debug("monthSchedules were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting all monthSchedules", e);
		}
		return monthSchedules;
	}

	@Override
	public MonthSchedule getTeacherMonthSchedule(Teacher teacher, String month) {
		logger.debug("trying get schedule in month={} for teacher={}", month, teacher);
		MonthSchedule monthSchedule = Optional.of(getMonthByName(month))
				.orElseThrow(() -> new EntityNotFoundException("MonthSchedule by month=" + month + " is not found!"));
		List<DaySchedule> daySchedules = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TEACHER_DAYS)) {
			preparedStatement.setInt(1, teacher.getId());
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				daySchedules.add(
						dayScheduleJdbcDao.getTeacherDaySchedule(teacher, resultSet.getDate("date").toLocalDate()));
			}
			logger.debug("month schedule for teacher was gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting a schedule in month for teacher", e);
		}
		monthSchedule.setWeekSchedules(
				DateUtil.divideDaysByWeeks(daySchedules.stream().distinct().collect(Collectors.toList())));
		return monthSchedule;
	}

	@Override
	public MonthSchedule getStudentMonthSchedule(Student student, String month) {
		logger.debug("trying get schedule in month={} for student={}", month, student);
		MonthSchedule monthSchedule = Optional.of(getMonthByName(month))
				.orElseThrow(() -> new EntityNotFoundException("MonthSchedule by month=" + month + " is not found!"));
		List<DaySchedule> daySchedules = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENT_DAYS)) {
			preparedStatement.setInt(1, student.getId());
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				daySchedules.add(
						dayScheduleJdbcDao.getStudentDaySchedule(student, resultSet.getDate("date").toLocalDate()));
			}
			logger.debug("month schedule for student was gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting a schedule in month for student", e);
		}
		monthSchedule.setWeekSchedules(
				DateUtil.divideDaysByWeeks(daySchedules.stream().distinct().collect(Collectors.toList())));
		return monthSchedule;
	}

	private MonthSchedule getMonthById(int id) {
		logger.debug("trying get MonthSchedule by id={}", id);
		MonthSchedule monthSchedule = null;
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MONTH_BY_ID)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				monthSchedule = new MonthSchedule();
				monthSchedule.setId(resultSet.getInt("month_id"));
				monthSchedule.setMonth(resultSet.getString("month_name"));
			}
			logger.debug("MonthSchedule was gotten by id={}", id);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a monthSchedule by id", e);
		}
		return monthSchedule;
	}

	private MonthSchedule getMonthByName(String monthName) {
		logger.debug("trying get MonthSchedule by month name={}", monthName);
		MonthSchedule monthSchedule = null;
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MONTH_BY_NAME)) {
			preparedStatement.setString(1, monthName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				monthSchedule = new MonthSchedule();
				monthSchedule.setId(resultSet.getInt("month_id"));
				monthSchedule.setMonth(resultSet.getString("month_name"));
			}
			logger.debug("MonthSchedule was gotten by month name={}", monthName);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a monthSchedule by month name", e);
		}
		return monthSchedule;
	}
}
