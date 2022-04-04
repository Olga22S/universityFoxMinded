CREATE DATABASE University;

CREATE TABLE audiences (
    audience_id INT PRIMARY KEY,
    number INT NOT NULL
);

CREATE TABLE disciplines (
    discipline_id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE lesson_times (
    lesson_time_id INT PRIMARY KEY,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

CREATE TABLE teachers (
    teacher_id INT PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL
);

CREATE TABLE groups (
    group_id INT PRIMARY KEY,
    group_name VARCHAR(20) NOT NULL
);

CREATE TABLE students (
    student_id INT PRIMARY KEY,
    group_id INT NOT NULL REFERENCES groups(group_id),
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL
);

CREATE TABLE teachers_disciplines (
    teacher_id INT NOT NULL REFERENCES teachers(teacher_id),
    discipline_id INT NOT NULL REFERENCES disciplines(discipline_id)
);

CREATE TABLE lessons (
    lesson_id INT PRIMARY KEY,
    discipline_id INT NOT NULL REFERENCES disciplines(discipline_id),
    audience_id INT NOT NULL REFERENCES audiences(audience_id),
    teacher_id INT NOT NULL REFERENCES teachers(teacher_id),
    group_id INT NOT NULL REFERENCES groups(group_id),
    lesson_time_id INT NOT NULL REFERENCES lesson_times(lesson_time_id)
);

CREATE TABLE day_schedules (
    day_schedule_id INT PRIMARY KEY,
    date DATE NOT NULL
);

CREATE TABLE day_schedules_lessons (
    day_schedule_id INT NOT NULL REFERENCES day_schedules(day_schedule_id),
    lesson_id INT NOT NULL REFERENCES lessons(lesson_id)
);

CREATE TABLE week_schedules (
    week_id INT PRIMARY KEY,
    week_number INT NOT NULL
);

CREATE TABLE week_schedules_day_schedules (
    week_id INT NOT NULL REFERENCES week_schedules(week_id),
    day_schedule_id INT NOT NULL REFERENCES day_schedules(day_schedule_id)
);

CREATE TABLE month_schedules (
    month_id INT PRIMARY KEY,
    month_name VARCHAR(20) NOT NULL
);

CREATE TABLE month_schedules_week_schedules (
    month_id INT NOT NULL REFERENCES month_schedules(month_id),
    week_id INT NOT NULL REFERENCES week_schedules(week_id)
);

CREATE TABLE schedules (
    schedule_id INT PRIMARY KEY,
    year INT NOT NULL
);

CREATE TABLE schedules_month_schedules (
    schedule_id INT NOT NULL REFERENCES schedules(schedule_id),
    month_id INT NOT NULL REFERENCES month_schedules(month_id)
);