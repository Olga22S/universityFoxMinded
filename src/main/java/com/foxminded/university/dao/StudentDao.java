package com.foxminded.university.dao;

import java.util.List;
import com.foxminded.university.entity.Student;

public interface StudentDao extends GenericDao<Student> {

	List<Student> getStudentsByGroupId(int id);
}
