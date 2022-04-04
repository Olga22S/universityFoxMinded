package com.foxminded.university.entity;

public class Lesson {

	private int id;
	private Discipline discipline;
	private Audience audience;
	private Teacher teacher;
	private Group group;
	private LessonTime lessonTime;

	public Lesson() {

	}

	public Lesson(int id, Discipline discipline, Audience audience, Teacher teacher, Group group,
			LessonTime lessonTime) {
		this.id = id;
		this.discipline = discipline;
		this.audience = audience;
		this.teacher = teacher;
		this.group = group;
		this.lessonTime = lessonTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	public Audience getAudience() {
		return audience;
	}

	public void setAudience(Audience audience) {
		this.audience = audience;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public LessonTime getLessonTime() {
		return lessonTime;
	}

	public void setLessonTime(LessonTime lessonTime) {
		this.lessonTime = lessonTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((audience == null) ? 0 : audience.hashCode());
		result = prime * result + ((discipline == null) ? 0 : discipline.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + id;
		result = prime * result + ((lessonTime == null) ? 0 : lessonTime.hashCode());
		result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
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
		if (!(obj instanceof Lesson)) {
			return false;
		}
		Lesson other = (Lesson) obj;
		if (audience == null) {
			if (other.audience != null) {
				return false;
			}
		} else if (!audience.equals(other.audience)) {
			return false;
		}
		if (discipline == null) {
			if (other.discipline != null) {
				return false;
			}
		} else if (!discipline.equals(other.discipline)) {
			return false;
		}
		if (group == null) {
			if (other.group != null) {
				return false;
			}
		} else if (!group.equals(other.group)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (lessonTime == null) {
			if (other.lessonTime != null) {
				return false;
			}
		} else if (!lessonTime.equals(other.lessonTime)) {
			return false;
		}
		if (teacher == null) {
			if (other.teacher != null) {
				return false;
			}
		} else if (!teacher.equals(other.teacher)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return lessonTime + " Discipline: " + discipline + "; " + audience + "; Teacher: " + teacher + "; Group: "
				+ group;
	}
}
