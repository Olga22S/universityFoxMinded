package com.foxminded.university.dao.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.dao.GenericDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.exception.NotTeacherDisciplineException;
import com.foxminded.university.utils.ConnectionProvider;

public class LessonJdbcDao implements GenericDao<Lesson> {

	private static final Logger logger = LoggerFactory.getLogger(LessonJdbcDao.class);
	private static final String INSERT = "INSERT INTO lessons (lesson_id, discipline_id, audience_id, teacher_id, group_id, lesson_time_id) VALUES(?,?,?,?,?,?)";
	private static final String SELECT = "SELECT * FROM lessons WHERE lesson_id=?";
	private static final String UPDATE = "UPDATE lessons SET lesson_time_id=?, group_id=?, teacher_id=?, audience_id=?, discipline_id=? "
			+ "WHERE lesson_id=?";
	private static final String DELETE = "DELETE FROM lessons WHERE lesson_id=?";
	private static final String SELECT_ALL = "SELECT * FROM lessons";
	private ConnectionProvider connectionProvider;
	private LessonTimeJdbcDao lessonTimeJdbcDao;
	private DisciplineJdbcDao disciplineJdbcDao;
	private AudienceJdbcDao audienceJdbcDao;
	private GroupJdbcDao groupJdbcDao;
	private TeacherJdbcDao teacherJdbcDao;

	public LessonJdbcDao(ConnectionProvider connectionProvider, LessonTimeJdbcDao lessonTimeJdbcDao,
			DisciplineJdbcDao disciplineJdbcDao, AudienceJdbcDao audienceJdbcDao, GroupJdbcDao groupJdbcDao,
			TeacherJdbcDao teacherJdbcDao) {
		this.connectionProvider = connectionProvider;
		this.lessonTimeJdbcDao = lessonTimeJdbcDao;
		this.disciplineJdbcDao = disciplineJdbcDao;
		this.audienceJdbcDao = audienceJdbcDao;
		this.groupJdbcDao = groupJdbcDao;
		this.teacherJdbcDao = teacherJdbcDao;
	}

	@Override
	public void add(Lesson lesson) {
		logger.debug("trying add new lesson={}", lesson);
		if (disciplineJdbcDao.getTeacherDisciplines(lesson.getTeacher()).stream()
				.noneMatch(d -> d.equals(lesson.getDiscipline()))) {
			throw new NotTeacherDisciplineException(lesson.getTeacher(), lesson.getDiscipline());
		}
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
			lesson.setId(NextIdGenerator.getNextId("lesson_sequence", connection));
			preparedStatement.setInt(1, lesson.getId());
			preparedStatement.setInt(2, lesson.getDiscipline().getId());
			preparedStatement.setInt(3, lesson.getAudience().getId());
			preparedStatement.setInt(4, lesson.getTeacher().getId());
			preparedStatement.setInt(5, lesson.getGroup().getId());
			preparedStatement.setInt(6, lesson.getLessonTime().getId());
			preparedStatement.executeUpdate();
			logger.debug("lesson was added");
		} catch (SQLException e) {
			throw new DaoException("Error when creating a lesson", e);
		}
	}

	@Override
	public Lesson getById(int id) {
		logger.debug("trying get lesson by id={}", id);
		Lesson lesson = null;
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				lesson = new Lesson();
				lesson.setId(resultSet.getInt("lesson_id"));
				lesson.setLessonTime(lessonTimeJdbcDao.getById(resultSet.getInt("lesson_time_id")));
				lesson.setDiscipline(disciplineJdbcDao.getById(resultSet.getInt("discipline_id")));
				lesson.setAudience(audienceJdbcDao.getById(resultSet.getInt("audience_id")));
				lesson.setGroup(groupJdbcDao.getById(resultSet.getInt("group_id")));
				lesson.setTeacher(teacherJdbcDao.getById(resultSet.getInt("teacher_id")));
			}
			logger.debug("lesson was gotten by id={}", id);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a lesson by id", e);
		}
		return lesson;
	}

	@Override
	public void update(Lesson lesson) {
		logger.debug("trying update lesson={}", lesson);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setInt(1, lesson.getLessonTime().getId());
			preparedStatement.setInt(2, lesson.getGroup().getId());
			preparedStatement.setInt(3, lesson.getTeacher().getId());
			preparedStatement.setInt(4, lesson.getAudience().getId());
			preparedStatement.setInt(5, lesson.getDiscipline().getId());
			preparedStatement.setInt(6, lesson.getId());
			preparedStatement.executeUpdate();
			logger.debug("lesson was updated");
		} catch (SQLException e) {
			throw new DaoException("Error when updating a lesson", e);
		}
	}

	@Override
	public void remove(Lesson lesson) {
		logger.debug("trying remove lesson={}", lesson);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
			preparedStatement.setInt(1, lesson.getId());
			preparedStatement.executeUpdate();
			logger.debug("lesson={} was removed", lesson);
		} catch (SQLException e) {
			throw new DaoException("Error when removing a lesson", e);
		}
	}

	@Override
	public List<Lesson> getAll() {
		logger.debug("trying get all lessons");
		List<Lesson> lessons = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
			while (resultSet.next()) {
				Lesson lesson = new Lesson();
				lesson.setId(resultSet.getInt("lesson_id"));
				lesson.setLessonTime(lessonTimeJdbcDao.getById(resultSet.getInt("lesson_time_id")));
				lesson.setDiscipline(disciplineJdbcDao.getById(resultSet.getInt("discipline_id")));
				lesson.setAudience(audienceJdbcDao.getById(resultSet.getInt("audience_id")));
				lesson.setGroup(groupJdbcDao.getById(resultSet.getInt("group_id")));
				lesson.setTeacher(teacherJdbcDao.getById(resultSet.getInt("teacher_id")));
				lessons.add(lesson);
			}
			logger.debug("lessons were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting all lessons", e);
		}
		return lessons;
	}
}
