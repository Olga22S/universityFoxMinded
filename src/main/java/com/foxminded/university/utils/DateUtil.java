package com.foxminded.university.utils;

import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import com.foxminded.university.entity.*;

public class DateUtil {

	public static List<WeekSchedule> divideDaysByWeeks(List<DaySchedule> days) {
		Map<Integer, List<DaySchedule>> weeksOfMonth = days.stream()
				.collect(Collectors.groupingBy(d -> d.getDate().get(WeekFields.of(Locale.getDefault()).weekOfMonth())));
		return weeksOfMonth.keySet().stream().map(k -> new WeekSchedule(k, k, weeksOfMonth.get(k)))
				.collect(Collectors.toList());
	}
}
