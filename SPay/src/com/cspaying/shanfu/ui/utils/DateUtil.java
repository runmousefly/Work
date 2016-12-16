package com.cspaying.shanfu.ui.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {

	public static Date getLastWeekSunday() {

		Calendar date = Calendar.getInstance(Locale.CHINA);

		date.setFirstDayOfWeek(Calendar.MONDAY);// 将每周第一天设为星期一，默认是星期天

		date.add(Calendar.WEEK_OF_MONTH, -1);// 周数减一，即上周

		date.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);// 日子设为星期天

		return date.getTime();
	}

	public static String[] getSpanDate(String beginDate, String endDate) {
		SimpleDateFormat inDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat outDateFormat = new SimpleDateFormat(
				"yy-MM-dd");
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);// 将每周第一天设为星期一，默认是星期天
		List<String> dates = new ArrayList<String>();
		Date b_date = null;
		Date e_date = null;
		try {
			b_date = inDateFormat.parse(beginDate);
			e_date = inDateFormat.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		int days = e_date.getDay() - b_date.getDay();
		
		calendar.setTime(b_date);
		
		for (int i = 0; i <= days; i++) {
			dates.add(outDateFormat.format(calendar.getTime()));
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		return dates.toArray(new String[dates.size()]);
	}
}
