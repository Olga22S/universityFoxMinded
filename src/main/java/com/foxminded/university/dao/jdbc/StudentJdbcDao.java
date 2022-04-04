package com.foxminded.university.dao.jdbc;

import java.sql.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.utils.ConnectionProvider;

public class StudentJdbcDao implements StudentDao {

	private static final Logger logger = LoggerFactory.getLogger(StudentJdbcDao.class);
	private static final String INSERT = "INSERT INTO students (student_id, first_name, last_name, group_id) VALUES(?,?,?,?)";
	private static final String SELECT = "SELECT * FROM students WHERE student_id = ?";
	private static final String UPDATE = "UPDATE students SET first_name=?, last_name=?, group_id=? WHERE student_id=?";
	private static final String DELETE = "DELETE FROM students WHERE student_id=?";
	private static final String SELECT_ALL = "SELECT student_id, first_name, last_name, group_id FROM students";
	private static final String SELECT_STUDENTS_FROM_GROUP = "SELECT *  FROM students WHERE group_id=?";
	private ConnectionProvider connectionProvider;

	public StudentJdbcDao(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	@Override
	public void add(Student student) {
		logger.debug("trying add new student={}", student);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
			student.setId(NextIdGenerator.getNextId("student_sequence", connection));
			preparedStatement.setInt(1, student.getId());
			preparedStatement.setString(2, student.getFirstName());
			preparedStatement.setString(3, student.getLastName());
			preparedStatement.setInt(4, (student.getGroupId()));
			preparedStatement.executeUpdate();
			logger.debug("student was added");
		} catch (SQLException e) {
			throw new DaoException("Error when creating a student", e);
		}
	}

	@Override
	public Student getById(int id) {
		logger.debug("trying get student by id={}", id);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return mapToStudent(resultSet);
			}
			logger.debug("student was gotten by id={}", id);
		} catch (SQLException e) {
			throw new DaoException("Error when getting a student by id", e);
		}
		return null;
	}

	@Override
	public void update(Student student) {
		logger.debug("trying update student={}", student);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
			preparedStatement.setString(1, student.getFirstName());
			preparedStatement.setString(2, student.getLastName());
			preparedStatement.setInt(3, student.getGroupId());
			preparedStatement.setInt(4, (student.getId()));
			preparedStatement.executeUpdate();
			logger.debug("student was updated");
		} catch (SQLException e) {
			throw new DaoException("Error when updating a student", e);
		}
	}

	@Override
	public void remove(Student student) {
		logger.debug("trying remove student={}", student);
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
			preparedStatement.setInt(1, student.getId());
			preparedStatement.executeUpdate();
			logger.debug("student={} was removed", student);
		} catch (SQLException e) {
			throw new DaoException("Error when removing a student", e);
		}
	}

	@Override
	public List<Student> getAll() {
		logger.debug("trying get all students");
		List<Student> students = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
			while (resultSet.next()) {
				students.add(getById(resultSet.getInt("student_id")));
			}
			logger.debug("students were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting all students", e);
		}
		return students;
	}

	@Override
	public List<Student> getStudentsByGroupId(int id) {
		logger.debug("trying get students by group id={}", id);
		List<Student> students = new ArrayList<>();
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENTS_FROM_GROUP)) {
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				students.add(mapToStudent(resultSet));
			}
			logger.debug("students by group id were gotten");
		} catch (SQLException e) {
			throw new DaoException("Error when getting the students by group id", e);
		}
		return students;
	}

	private Student mapToStudent(ResultSet resultSet) throws SQLException {
		return new Student(resultSet.getInt("student_id"), resultSet.getString("first_name"),
				resultSet.getString("last_name"), resultSet.getInt("group_id"));
	}
}
