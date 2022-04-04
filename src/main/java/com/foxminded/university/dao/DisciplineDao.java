package com.foxminded.university.dao;

import java.util.List;
import com.foxminded.university.entity.*;

public interface DisciplineDao extends GenericDao<Discipline> {

	List<Discipline> getTeacherDisciplines(Teacher teacher);
}
