INSERT INTO audiences(audience_id, number) VALUES
('1','111'),
('2','222'),
('3','333'),
('4','444'),
('5','555'),
('6','666'),
('7','777');

INSERT INTO disciplines(discipline_id, name) VALUES
('1','Mathematics'),
('2','Informatics'),
('3','Economy'),
('4','Physics');

INSERT INTO lesson_times(lesson_time_id, start_time, end_time) VALUES
('1','09:00','10:30'),
('2','10:30','12:00'),
('3','12:00','13:30'),
('4','14:00','15:30'),
('5','16:00','17:30');

INSERT INTO teachers(teacher_id, first_name, last_name) VALUES 
('1','IRINA','PETROVNA'),
('2', 'ANDREY', 'SMIRNOV'),
('3', 'SERGEY', 'KOVAL'),
('4', 'TAMARA', 'NAZAROVA');

INSERT INTO groups(group_id, group_name) VALUES
('1','FIRST'),
('2','SECOND'),
('3','THIRD'),
('4','FOURTH'),
('5','FIFTH');

INSERT INTO students(student_id, group_id, first_name, last_name) VALUES
('1','1', 'OLGA','PYSHKINA'),
('2','1', 'MAKSIM','MANAEV'),
('3','2', 'ILYA','KRUTOV'),
('4','2', 'ARINA','PYSHKINA'),
('5','3', 'KATE','KREDYSHEVA'),
('6','3', 'GOSHA','YAKOVENKO'),
('7','4', 'ANTON','SERGEEV'),
('8','4', 'MARINA','IVANOVA'),
('9','5', 'ALEX','ORLOV');

INSERT INTO teachers_disciplines(teacher_id, discipline_id) VALUES 
('1', '1'),
('1', '2'),
('2', '1'),
('2', '3'),
('3', '1'),
('4', '1'),
('4', '3'),
('4', '4');

INSERT INTO lessons(
lesson_id, discipline_id, audience_id, teacher_id, group_id, lesson_time_id)
	VALUES 
('1', '1', '1', '1', '1', '1'),
('2', '1', '2', '3', '2', '1'),
('3', '2', '2', '1', '2', '2'),
('4', '1', '1', '2', '2', '3'),
('5', '3', '1', '2', '1', '3'),
('6', '3', '3', '2', '1', '1'),
('7', '1', '3', '1', '1', '2'),
('8', '3', '3', '2', '1', '2'),
('9', '1', '5', '1', '5', '2');

INSERT INTO day_schedules(day_schedule_id, date) VALUES
('1', '2019-05-01'),
('2', '2019-05-06'),
('3', '2019-05-08'),
('4', '2019-05-15');

INSERT INTO day_schedules_lessons(day_schedule_id, lesson_id) VALUES
('1', '1'),
('1', '2'),
('1', '8'),
('2', '3'),
('2', '4'),
('2', '6'),
('3', '3'),
('3', '4'),
('3', '6'), 
('4', '9');

INSERT INTO week_schedules(week_id, week_number) VALUES
('1', '1'),
('2', '2'), 
('3', '3');

INSERT INTO week_schedules_day_schedules(week_id, day_schedule_id) VALUES
('1', '1'),
('2', '2'),
('2', '3'), 
('3', '4');

INSERT INTO month_schedules(month_id, month_name) VALUES 
('1', 'May');

INSERT INTO month_schedules_week_schedules(month_id, week_id) VALUES 
('1', '1'),
('1', '2'),
('1', '3');

INSERT INTO schedules(schedule_id, year) VALUES
('1', '2019'), 
('2', '2020');

INSERT INTO schedules_month_schedules(schedule_id, month_id) VALUES 
('1', '1');