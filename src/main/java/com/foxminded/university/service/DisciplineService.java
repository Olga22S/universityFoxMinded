package com.foxminded.university.service;

import static java.util.Objects.isNull;
import java.util.*;
import com.foxminded.university.dao.jdbc.DisciplineJdbcDao;
import com.foxminded.university.entity.Discipline;
import com.foxminded.university.entity.Teacher;
import com.foxminded.university.exception.EntityNotFoundException;

public class DisciplineService {

	private DisciplineJdbcDao disciplineJdbcDao;

	public DisciplineService(DisciplineJdbcDao disciplineJdbcDao) {
		this.disciplineJdbcDao = disciplineJdbcDao;
	}

	public void add(Discipline discipline) {
		disciplineJdbcDao.add(discipline);
	}

	public Discipline getById(int id) {
		Discipline discipline = disciplineJdbcDao.getById(id);
		if (isNull(discipline)) {
			throw new EntityNotFoundException("Discipline by id=" + id + " was not found");
		} else {
			return discipline;
		}
	}

	public void update(Discipline discipline) {
		disciplineJdbcDao.update(discipline);
	}

	public void remove(Discipline discipline) {
		disciplineJdbcDao.remove(discipline);
	}

	public List<Discipline> getDisciplines() {
		return disciplineJdbcDao.getAll();
	}

	public List<Discipline> getTeacherDisciplines(Teacher teacher) {
		return disciplineJdbcDao.getTeacherDisciplines(teacher);
	}
}
