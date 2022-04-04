package com.foxminded.university.dao.jdbc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.dao.DayScheduleDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.utils.ConnectionProvider;

public class DayScheduleJdbcDao implements DayScheduleDao {

	private static final Logger logger = LoggerFactory.getLogger(DayScheduleJdbcDao.class);
	private static final String INSERT_INTO_DAY_SCHEDULES = "INSERT INTO day_schedules (day_schedule_id, date) VALUES(?,?)";
	private static final String INSERT_INTO_DAY_SCHEDULES_LESSONS = "INSERT INTO day_schedules_lessons (day_schedule_id, lesson_id) VALUES(?,?)";
	private static final String SELECT_BY_DATE = "SELECT day_schedule_id, date FROM day_schedules WHERE date =?";
	private static final String SELECT_BY_ID = "SELECT day_schedule_id, date FROM day_schedules WHERE day_schedule_id =?";
	private static final String UPDATE = "UPDATE day_schedules SET date=? WHERE day_schedule_id=?";
	private static final String DELETE = "DELETE FROM day_schedules WHERE day_schedule_id=?";
	private static final String SELECT_ALL = "SELECT day_schedule_id FROM day_schedules";
	private static final String SELECT_FOR_STUDENT = "SELECT lesson_id "
			+ "FROM day_schedules_lessons JOIN day_schedules USING (day_schedule_id) "
			+ "JOIN lessons USING (lesson_id) JOIN students USING (group_id) WHERE date =? AND student_id=?";
	private static final String SELECT_FOR_TEACHER = "SELECT lesson_id FROM day_schedules_lessons "
			+ "JOIN day_schedules USING (day_schedule_id) JOIN lessons USING (lesson_id)"
			+ "JOIN teachers USING (teacher_id) WHERE date =? AND teacher_id=?";
	private static final String SELECT_LESSONS = "SELECT lesson_id FROM day_schedules_lessons WHERE day_schedule_id = ?";
	private ConnectionProvider connectionProvider;
	private LessonJdbcDao lessonJdbcDao;

	public DayScheduleJdbcDao(ConnectionProvider connectionProvider, LessonJdbcDao lessonJdbcDao) {
		this.connectionProvider = connectionProvider;
		this.lessonJdbcDao = lessonJdbcDao;
	}

	@Override
	public DaySchedule getById(int id) {
		DaySchedule daySchedule = Optional.of(getDayScheduleById(id))
				.orElseThrow(() -> new EntityNotFoundException("DaySchedule by id=" + id + " is not found!"));
		List<Lesson> dayLessons = new ArrayList<>();
		logger.debug("trying get DaySchedule by id={}", id);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LESSONS)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				dayLessons.add(lessonJdbcDao.getById(resultSet.getInt("lesson_id")));
			}
		} catch (SQLException e) {
			throw new DaoException("Error when getting DaySchedule by id", e);
		}
		daySchedule.setLessons(dayLessons);
		logger.debug("DaySchedule was gotten by id={}", id);
		return daySchedule;
	}

	@Override
	public DaySchedule getByDate(LocalDate date) {
		DaySchedule daySchedule = Optional.of(getDayScheduleByDate(date))
				.orElseThrow(() -> new EntityNotFoundException("DaySchedule by date=" + date + " is not found!"));
		daySchedule.setLessons(getById(daySchedule.getId()).getLessons());
		logger.debug("get DaySchedule by date={}", date);
		return daySchedule;
	}

	@Override
	public void add(DaySchedule daySchedule) {
		logger.debug("trying add new DaySchedule={}", daySchedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_DAY_SCHEDULES)) {
			daySchedule.setId(NextIdGenerator.getNextId("day_schedule_sequence", connection));
			preparedStatement.setInt(1, daySchedule.getId());
			preparedStatement.setDate(2, Date.valueOf(daySchedule.getDate()));
			preparedStatement.executeUpdate();
			logger.debug("DaySchedule was added");
		} catch (SQLException e) {
			throw new DaoException("Error when creating DaySchedule", e);
		}
	}

	public void addLesson(Lesson lesson, DaySchedule daySchedule) {
		logger.debug("trying add lesson={} in daySchedule={}", lesson, daySchedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_DAY_SCHEDULES_LESSONS)) {
			preparedStatement.setInt(1, daySchedule.getId());
			preparedStatement.setInt(2, lesson.getId());
			preparedStatement.executeUpdate();
			logger.debug("lesson was added in daySchedule");
		} catch (SQLException e) {
			throw new DaoException("Error when adding lesson in daySchedule", e);
		}
	}

	@Override
	public void update(DaySchedule daySchedule) {
		logger.debug("trying update DaySchedule={}", daySchedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setDate(1, Date.valueOf(daySchedule.getDate()));
			preparedStatement.setInt(2, daySchedule.getId());
			preparedStatement.executeUpdate();
			logger.debug("DaySchedule was updated");
		} catch (SQLException e) {
			throw new DaoException("Error when updating DaySchedule", e);
		}
	}

	@Override
	public void remove(DaySchedule daySchedule) {
		logger.debug("trying remove daySchedule={}", daySchedule);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE);) {
			preparedStatement.setInt(1, daySchedule.getId());
			preparedStatement.executeUpdate();
			logger.debug("daySchedule={} was removed", daySchedule);
		} catch (SQLException e) {
			throw new DaoException("Error when removing daySchedule", e);
		}
	}

	@Override
	public List<DaySchedule> getAll() {
		logger.debug("trying get all DaySchedules");
		List<DaySchedule> daySchedules = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
			while (resultSet.next()) {
				daySchedules.add(getById(resultSet.getInt("day_schedule_id")));
			}
			logger.debug("DaySchedules were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when geting all daySchedules", e);
		}
		return daySchedules;
	}

	@Override
	public DaySchedule getStudentDaySchedule(Student student, LocalDate date) {
		logger.debug("getting StudentDaySchedule for student={} by date={}", student, date);
		return getPersonDaySchedule(student, date, SELECT_FOR_STUDENT);
	}

	@Override
	public DaySchedule getTeacherDaySchedule(Teacher teacher, LocalDate date) {
		logger.debug("getting TeacherDaySchedule for teacher={} by date={}", teacher, date);
		return getPersonDaySchedule(teacher, date, SELECT_FOR_TEACHER);
	}

	private DaySchedule getPersonDaySchedule(Person person, LocalDate date, String sql) {
		logger.debug("trying get PersonDaySchedule for person={} by date={}", person, date);
		DaySchedule daySchedule = Optional.of(getDayScheduleByDate(date)).orElseThrow(
				() -> new EntityNotFoundException("Person DaySchedule by date=" + date + " is not found!"));
		List<Lesson> dayLessons = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setDate(1, Date.valueOf(date));
			preparedStatement.setInt(2, person.getId());
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				dayLessons.add((lessonJdbcDao.getById(resultSet.getInt("lesson_id"))));
			}
			logger.debug("PersonDaySchedule for person={} by date={} was gotten", person, date);
		} catch (SQLException e) {
			throw new DaoException("Error when geting PersonDaySchedule", e);
		}
		daySchedule.setLessons(dayLessons);
		return daySchedule;
	}

	private DaySchedule getDayScheduleByDate(LocalDate date) {
		logger.debug("trying get DaySchedule by date={}", date);
		DaySchedule daySchedule = null;
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_DATE)) {
			preparedStatement.setDate(1, Date.valueOf(date));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				daySchedule = new DaySchedule();
				daySchedule.setId(resultSet.getInt("day_schedule_id"));
				daySchedule.setDate(resultSet.getDate("date").toLocalDate());
			}
			logger.debug("DaySchedule by date={} was gotted", date);
		} catch (SQLException e) {
			throw new DaoException("Error when getting DaySchedule by date", e);
		}
		return daySchedule;
	}

	private DaySchedule getDayScheduleById(int id) {
		logger.debug("trying get DaySchedule by id={}", id);
		DaySchedule daySchedule = null;
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				daySchedule = new DaySchedule();
				daySchedule.setId(resultSet.getInt("day_schedule_id"));
				daySchedule.setDate(resultSet.getDate("date").toLocalDate());
			}
			logger.debug("DaySchedule by id={} was gotten", id);
		} catch (SQLException e) {
			throw new DaoException("Error when getting DaySchedule by id", e);
		}
		return daySchedule;
	}
}
