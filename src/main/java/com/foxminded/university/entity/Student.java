package com.foxminded.university.entity;

public class Student extends Person {

	private int groupId;

	public Student() {

	}

	public Student(int id, String firstName, String lastName, int groupId) {
		super(id, firstName, lastName);
		this.groupId = groupId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int group_id) {
		this.groupId = group_id;
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName();
	}
}
