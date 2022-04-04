package com.foxminded.university;

import java.io.IOException;
import java.time.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foxminded.university.dao.jdbc.*;
import com.foxminded.university.exception.DaoException;
import com.foxminded.university.service.*;
import com.foxminded.university.utils.ConnectionProvider;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	static StudentService studentService;
	static TeacherService teacherService;
	static DayScheduleService dayScheduleService;
	static MonthScheduleService monthScheduleService;

	public static void main(String[] args) throws IOException {
		ConnectionProvider connectionProvider = new ConnectionProvider();
		dayScheduleService = new DayScheduleService(getDayScheduleJdbcDao(connectionProvider));
		monthScheduleService = new MonthScheduleService(new MonthScheduleJdbcDao(connectionProvider,
				new WeekScheduleJdbcDao(connectionProvider, getDayScheduleJdbcDao(connectionProvider)),
				getDayScheduleJdbcDao(connectionProvider)));
		studentService = new StudentService(new StudentJdbcDao(connectionProvider));
		teacherService = new TeacherService(new TeacherJdbcDao(connectionProvider));
		System.out.println("Choose action: " + "\n1 - get schedule for month\n2 - get schedule for day"
				+ "\n3 - student schedule for day\n4 - student schedule for month"
				+ "\n5 - teacher schedule for day\n6 - teacher schedule for month");
		Scanner scanner = new Scanner(System.in);
		int i = scanner.nextInt();
		switch (i) {
		case (1):
			printMonthSchedule(scanner);
			break;
		case (2):
			printDaySchedule(scanner);
			break;
		case (3):
			printStudentDaySchedule(scanner);
			break;
		case (4):
			printStudentMonthSchedule(scanner);
			break;
		case (5):
			printTeacherDaySchedule(scanner);
			break;
		case (6):
			printTeacherMonthSchedule(scanner);
			break;
		default:
			System.out.println("Command is not found!");
			break;
		}
	}

	private static DayScheduleJdbcDao getDayScheduleJdbcDao(ConnectionProvider connectionProvider) {
		AudienceJdbcDao audienceJdbcDao = new AudienceJdbcDao(connectionProvider);
		DisciplineJdbcDao disciplineJdbcDao = new DisciplineJdbcDao(connectionProvider);
		LessonTimeJdbcDao lessonTimeJdbcDao = new LessonTimeJdbcDao(connectionProvider);
		GroupJdbcDao groupJdbcDao = new GroupJdbcDao(connectionProvider);
		TeacherJdbcDao teacherJdbcDao = new TeacherJdbcDao(connectionProvider);
		LessonJdbcDao lessonJdbcDao = new LessonJdbcDao(connectionProvider, lessonTimeJdbcDao, disciplineJdbcDao,
				audienceJdbcDao, groupJdbcDao, teacherJdbcDao);
		return new DayScheduleJdbcDao(connectionProvider, lessonJdbcDao);
	}

	private static void printMonthSchedule(Scanner scanner) {
		try {
			monthScheduleService.getByMonth(getEnteredMonth(scanner)).getWeekSchedules().stream()
					.map(s -> s.getDaySchedules()).forEach(System.out::println);
		} catch (DaoException e) {
			logger.error("exception when getting schedule for month={} ", e, getEnteredMonth(scanner));
		}
	}

	private static void printDaySchedule(Scanner scanner) {
		try {
			dayScheduleService.getByDate(getEnteredDate(scanner)).getLessons().forEach(System.out::println);
		} catch (DaoException e) {
			logger.error("exception when getting schedule for date={} ", e, getEnteredDate(scanner));
		}
	}

	private static void printStudentDaySchedule(Scanner scanner) {
		System.out.println("Type student id: ");
		try {
			dayScheduleService.getStudentDaySchedule(studentService.getById(scanner.nextInt()), getEnteredDate(scanner))
					.getLessons().forEach(System.out::println);
		} catch (DaoException e) {
			logger.error("exception when getting day schedule for student ", e);
		}
	}

	private static void printStudentMonthSchedule(Scanner scanner) {
		System.out.println("Type student id: ");
		try {
			monthScheduleService
					.getStudentMonthSchedule(studentService.getById(scanner.nextInt()), getEnteredMonth(scanner))
					.getWeekSchedules().forEach(s -> s.getDaySchedules().forEach(System.out::println));
		} catch (DaoException e) {
			logger.error("exception when getting month schedule for student ", e);
		}
	}

	private static void printTeacherDaySchedule(Scanner scanner) {
		System.out.println("Type teacher id: ");
		try {
			dayScheduleService.getTeacherDaySchedule(teacherService.getById(scanner.nextInt()), getEnteredDate(scanner))
					.getLessons().forEach(System.out::println);
		} catch (DaoException e) {
			logger.error("exception when getting day schedule for teacher ", e);
		}
	}

	private static void printTeacherMonthSchedule(Scanner scanner) {
		System.out.println("Type teacher id: ");
		try {
			monthScheduleService
					.getTeacherMonthSchedule(teacherService.getById(scanner.nextInt()), getEnteredMonth(scanner))
					.getWeekSchedules().forEach(s -> s.getDaySchedules().forEach(System.out::println));
		} catch (DaoException e) {
			logger.error("exception when getting month schedule for teacher ", e);
		}
	}

	private static String getEnteredMonth(Scanner scanner) {
		System.out.println("Type month: ");
		return scanner.next();
	}

	private static LocalDate getEnteredDate(Scanner scanner) {
		System.out.println("Type date in format yyyy-mm-dd: ");
		return LocalDate.parse(scanner.next());
	}
}
