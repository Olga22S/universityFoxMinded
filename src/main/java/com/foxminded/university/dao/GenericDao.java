package com.foxminded.university.dao;

import java.util.List;

public interface GenericDao<T> {

	void add(T t);

	T getById(int id);

	void update(T t);

	void remove(T t);

	List<T> getAll();

}
