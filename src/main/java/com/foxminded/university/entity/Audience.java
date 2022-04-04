package com.foxminded.university.entity;

public class Audience {

	private int id;
	private int number;

	public Audience() {

	}

	public Audience(int id, int number) {
		this.id = id;
		this.number = number;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getId() {
		return id;
	}

	public int getNumber() {
		return number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Audience)) {
			return false;
		}
		Audience other = (Audience) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Audience: " + number;
	}
}
