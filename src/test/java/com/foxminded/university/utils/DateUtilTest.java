package com.foxminded.university.utils;

import static org.junit.Assert.*;

import java.time.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import com.foxminded.university.entity.DaySchedule;
import com.foxminded.university.entity.WeekSchedule;

public class DateUtilTest {

	private DaySchedule daySchedule1;
	private DaySchedule daySchedule2;
	private DaySchedule daySchedule3;
	private DaySchedule daySchedule4;

	@Before
	public void setUp() throws Exception {
		daySchedule1 = new DaySchedule(1, LocalDate.of(2019, Month.SEPTEMBER, 2));
		daySchedule2 = new DaySchedule(2, LocalDate.of(2019, Month.SEPTEMBER, 9));
		daySchedule3 = new DaySchedule(3, LocalDate.of(2019, Month.SEPTEMBER, 16));
		daySchedule4 = new DaySchedule(4, LocalDate.of(2019, Month.SEPTEMBER, 23));
	}

	@Test
	public void daySchedules_divideDaysByWeeks_weekSchedules() {
		List<WeekSchedule> expected = getExpectedWeekSchedules();

		List<DaySchedule> daySchedules = new ArrayList<>();
		daySchedules.add(daySchedule1);
		daySchedules.add(daySchedule2);
		daySchedules.add(daySchedule3);
		daySchedules.add(daySchedule4);
		List<WeekSchedule> result = DateUtil.divideDaysByWeeks(daySchedules);

		assertEquals(expected, result);
	}

	private List<WeekSchedule> getExpectedWeekSchedules() {
		List<WeekSchedule> expected = new ArrayList<>();
		List<DaySchedule> firstWeekDaySchedules = new ArrayList<>();
		firstWeekDaySchedules.add(daySchedule1);
		WeekSchedule weekSchedule1 = new WeekSchedule(1, 1, firstWeekDaySchedules);
		List<DaySchedule> secondWeekDaySchedules = new ArrayList<>();
		secondWeekDaySchedules.add(daySchedule2);
		WeekSchedule weekSchedule2 = new WeekSchedule(2, 2, secondWeekDaySchedules);
		List<DaySchedule> thirdWeekDaySchedules = new ArrayList<>();
		thirdWeekDaySchedules.add(daySchedule3);
		WeekSchedule weekSchedule3 = new WeekSchedule(3, 3, thirdWeekDaySchedules);
		List<DaySchedule> fourthWeekDaySchedules = new ArrayList<>();
		fourthWeekDaySchedules.add(daySchedule4);
		WeekSchedule weekSchedule4 = new WeekSchedule(4, 4, fourthWeekDaySchedules);
		expected.add(weekSchedule1);
		expected.add(weekSchedule2);
		expected.add(weekSchedule3);
		expected.add(weekSchedule4);
		return expected;
	}
}
