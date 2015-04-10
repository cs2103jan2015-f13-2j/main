package parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import resource.KeywordConstant;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class DateFixer {

	private static int delayInMillisec = 4000; // 4 sec optimal delay time for
												// process

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	protected static Date fixStartDate(Date inDate) {

		Calendar inCalendar = Calendar.getInstance();
		Calendar auxCalendar = Calendar.getInstance();
		Calendar nowCalendar = Calendar.getInstance();

		inCalendar.setTime(inDate);
		auxCalendar.set(Calendar.SECOND, inCalendar.get(Calendar.SECOND));
		auxCalendar.set(Calendar.MINUTE, inCalendar.get(Calendar.MINUTE));
		auxCalendar.set(Calendar.HOUR_OF_DAY,
				inCalendar.get(Calendar.HOUR_OF_DAY));

		long timeDifferenceLong = auxCalendar.getTime().getTime()
				- nowCalendar.getTime().getTime();

		int timeDifference = new Long(timeDifferenceLong).intValue();

		if (0 <= timeDifference && timeDifference <= delayInMillisec) {
			inCalendar.set(Calendar.HOUR_OF_DAY, 0);
			inCalendar.set(Calendar.MINUTE, 0);
			inCalendar.set(Calendar.SECOND, 1);
		}

		Date processedDate = inCalendar.getTime();

		return processedDate;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	protected static Date fixEndDate(Date inDate) {

		Calendar inCalendar = Calendar.getInstance();
		Calendar auxCalendar = Calendar.getInstance();
		Calendar nowCalendar = Calendar.getInstance();

		inCalendar.setTime(inDate);
		auxCalendar.set(Calendar.SECOND, inCalendar.get(Calendar.SECOND));
		auxCalendar.set(Calendar.MINUTE, inCalendar.get(Calendar.MINUTE));
		auxCalendar.set(Calendar.HOUR_OF_DAY,
				inCalendar.get(Calendar.HOUR_OF_DAY));

		long timeDifferenceLong = auxCalendar.getTime().getTime()
				- nowCalendar.getTime().getTime();

		int timeDifference = new Long(timeDifferenceLong).intValue();

		if (0 <= timeDifference && timeDifference <= delayInMillisec) {
			inCalendar.set(Calendar.HOUR_OF_DAY, 23);
			inCalendar.set(Calendar.MINUTE, 59);
			inCalendar.set(Calendar.SECOND, 59);
		}

		Date processedDate = inCalendar.getTime();

		return processedDate;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	protected static List<Date> parseStringToDate(String dateInfo) {

		List<Date> dates = new ArrayList<Date>();

		if (dateInfo != null) {

			Parser parser = new Parser();

			List<DateGroup> groups = parser.parse(dateInfo.trim());

			if (!groups.isEmpty()) {
				DateGroup firstDate = groups.get(0);
				dates = firstDate.getDates();
			}
		}
		return dates;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	protected static Date fixStartDateDisplay(Date inDate, String displayType) {

		Calendar inCalendar = Calendar.getInstance();
		Calendar auxCalendar = Calendar.getInstance();
		Calendar nowCalendar = Calendar.getInstance();

		inCalendar.setTime(inDate);
		auxCalendar.set(Calendar.SECOND, inCalendar.get(Calendar.SECOND));
		auxCalendar.set(Calendar.MINUTE, inCalendar.get(Calendar.MINUTE));
		auxCalendar.set(Calendar.HOUR_OF_DAY,
				inCalendar.get(Calendar.HOUR_OF_DAY));

		long timeDifferenceLong = auxCalendar.getTime().getTime()
				- nowCalendar.getTime().getTime();

		int timeDifference = new Long(timeDifferenceLong).intValue();

		int minDateInMonth = inCalendar.getActualMinimum(Calendar.DATE);
		int minMonthInYear = inCalendar.getActualMinimum(Calendar.MONTH);
		int minHour = inCalendar.getActualMinimum(Calendar.HOUR_OF_DAY);
		int minMinute = inCalendar.getActualMinimum(Calendar.MINUTE);
		int minSec = inCalendar.getActualMinimum(Calendar.SECOND);

		if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_DAY)
				|| displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_DATE)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.HOUR_OF_DAY, minHour);
				inCalendar.set(Calendar.MINUTE, minMinute);
				inCalendar.set(Calendar.SECOND, minSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_WEEK)) {

			Calendar firstDateOfWeek = (Calendar) inCalendar.clone();
			firstDateOfWeek.add(
					Calendar.DAY_OF_WEEK,
					firstDateOfWeek.getFirstDayOfWeek()
							- firstDateOfWeek.get(Calendar.DAY_OF_WEEK));

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {
				inCalendar = (Calendar) firstDateOfWeek.clone();
				inCalendar.set(Calendar.HOUR_OF_DAY, minHour);
				inCalendar.set(Calendar.MINUTE, minMinute);
				inCalendar.set(Calendar.SECOND, minSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_MONTH)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.DATE, minDateInMonth);
				inCalendar.set(Calendar.HOUR_OF_DAY, minHour);
				inCalendar.set(Calendar.MINUTE, minMinute);
				inCalendar.set(Calendar.SECOND, minSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_YEAR)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.DATE, minDateInMonth);
				inCalendar.set(Calendar.MONTH, minMonthInYear);
				inCalendar.set(Calendar.HOUR_OF_DAY, minHour);
				inCalendar.set(Calendar.MINUTE, minMinute);
				inCalendar.set(Calendar.SECOND, minSec);
			}
		}

		Date processedDate = inCalendar.getTime();

		return processedDate;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	protected static Date fixEndDateDisplay(Date inDate, String displayType) {

		Calendar inCalendar = Calendar.getInstance();
		Calendar auxCalendar = Calendar.getInstance();
		Calendar nowCalendar = Calendar.getInstance();

		inCalendar.setTime(inDate);
		auxCalendar.set(Calendar.SECOND, inCalendar.get(Calendar.SECOND));
		auxCalendar.set(Calendar.MINUTE, inCalendar.get(Calendar.MINUTE));
		auxCalendar.set(Calendar.HOUR_OF_DAY,
				inCalendar.get(Calendar.HOUR_OF_DAY));

		long timeDifferenceLong = auxCalendar.getTime().getTime()
				- nowCalendar.getTime().getTime();

		int timeDifference = new Long(timeDifferenceLong).intValue();

		int maxDateInMonth = inCalendar.getActualMaximum(Calendar.DATE);
		int maxMonthInYear = inCalendar.getActualMaximum(Calendar.MONTH);
		int maxHour = inCalendar.getActualMaximum(Calendar.HOUR_OF_DAY);
		int maxMinute = inCalendar.getActualMaximum(Calendar.MINUTE);
		int maxSec = inCalendar.getActualMaximum(Calendar.SECOND);

		if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_DAY)
				|| displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_DATE)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.HOUR_OF_DAY, maxHour);
				inCalendar.set(Calendar.MINUTE, maxMinute);
				inCalendar.set(Calendar.SECOND, maxSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_WEEK)) {

			Calendar firstDateOfWeek = (Calendar) inCalendar.clone();
			firstDateOfWeek.add(
					Calendar.DAY_OF_WEEK,
					firstDateOfWeek.getFirstDayOfWeek()
							- firstDateOfWeek.get(Calendar.DAY_OF_WEEK));

			Calendar lastDateOfWeek = (Calendar) firstDateOfWeek.clone();
			lastDateOfWeek.add(Calendar.DATE, 6);

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {
				inCalendar = (Calendar) lastDateOfWeek.clone();
				inCalendar.set(Calendar.HOUR_OF_DAY, maxHour);
				inCalendar.set(Calendar.MINUTE, maxMinute);
				inCalendar.set(Calendar.SECOND, maxSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_MONTH)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.DATE, maxDateInMonth);
				inCalendar.set(Calendar.HOUR_OF_DAY, maxHour);
				inCalendar.set(Calendar.MINUTE, maxMinute);
				inCalendar.set(Calendar.SECOND, maxSec);
			}

		} else if (displayType.equalsIgnoreCase(KeywordConstant.KEYWORD_YEAR)) {

			if (0 <= timeDifference && timeDifference <= delayInMillisec) {

				inCalendar.set(Calendar.DATE, maxDateInMonth);
				inCalendar.set(Calendar.MONTH, maxMonthInYear);
				inCalendar.set(Calendar.HOUR_OF_DAY, maxHour);
				inCalendar.set(Calendar.MINUTE, maxMinute);
				inCalendar.set(Calendar.SECOND, maxSec);
			}
		}

		Date processedDate = inCalendar.getTime();

		return processedDate;
	}
}
