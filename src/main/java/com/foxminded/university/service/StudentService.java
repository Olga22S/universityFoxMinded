package com.foxminded.university.service;

import static java.util.Objects.isNull;
import java.util.*;
import com.foxminded.university.dao.jdbc.StudentJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.EntityNotFoundException;

public class StudentService {

	private StudentJdbcDao studentJdbcDao;

	public StudentService(StudentJdbcDao studentJdbcDao) {
		this.studentJdbcDao = studentJdbcDao;
	}

	public void add(Student student) {
		studentJdbcDao.add(student);
	}

	public Student getById(int id) {
		Student student = studentJdbcDao.getById(id);
		if (isNull(student)) {
			throw new EntityNotFoundException("Student by id=" + id + " was not found");
		} else {
			return student;
		}
	}

	public void update(Student student) {
		studentJdbcDao.update(student);
	}

	public void remove(Student student) {
		studentJdbcDao.remove(student);
	}

	public List<Student> getStudents() {
		return studentJdbcDao.getAll();
	}

	public List<Student> getStudentsByGroupId(int id) {
		return studentJdbcDao.getStudentsByGroupId(id);
	}
}
