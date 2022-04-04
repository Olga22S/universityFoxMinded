package com.foxminded.university.entity;

import java.util.List;

public class Teacher extends Person {

	private List<Discipline> disciplines;

	public Teacher() {

	}

	public Teacher(int id, String firstName, String lastName) {
		super(id, firstName, lastName);
	}

	public List<Discipline> getDisciplines() {
		return disciplines;
	}

	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName();
	}
}
