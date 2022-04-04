package com.foxminded.university.service;

import static java.util.Objects.isNull;
import java.util.*;
import com.foxminded.university.dao.jdbc.TeacherJdbcDao;
import com.foxminded.university.entity.*;
import com.foxminded.university.exception.EntityNotFoundException;

public class TeacherService {

	private TeacherJdbcDao teacherJdbcDao;

	public TeacherService(TeacherJdbcDao teacherJdbcDao) {
		this.teacherJdbcDao = teacherJdbcDao;
	}

	public void add(Teacher teacher) {
		teacherJdbcDao.add(teacher);
	}

	public void addDisciplinesForTeacher(Teacher teacher, List<Discipline> disciplines) {
		teacherJdbcDao.addDisciplinesForTeacher(teacher, disciplines);
	}

	public Teacher getById(int id) {
		Teacher teacher = teacherJdbcDao.getById(id);
		if (isNull(teacher)) {
			throw new EntityNotFoundException("Teacher by id="+id+" was not found");
		} else {
			return teacher;
		}
	}

	public void update(Teacher teacher) {
		teacherJdbcDao.update(teacher);
	}

	public void remove(Teacher teacher) {
		teacherJdbcDao.remove(teacher);
	}

	public List<Teacher> getTeachers() {
		return teacherJdbcDao.getAll();
	}
}
