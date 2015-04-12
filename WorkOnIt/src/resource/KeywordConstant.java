package resource;

import java.util.HashMap;
import java.util.Map;

public class KeywordConstant {

	public static final String KEYWORD_ADD = "add";
	public static final String KEYWORD_UPDATE = "update";
	public static final String KEYWORD_DELETE = "delete";
	public static final String KEYWORD_RETRIEVE = "retrieve";
	public static final String KEYWORD_BY = "by";
	public static final String KEYWORD_ON = "on";
	public static final String KEYWORD_AT = "at";
	public static final String KEYWORD_FROM = "from";
	public static final String KEYWORD_SINCE = "since";
	public static final String KEYWORD_TO = "to";
	public static final String KEYWORD_EVERY = "every";
	public static final String KEYWORD_PRIORITY = "priority";
	public static final String KEYWORD_UNDO = "undo";
	public static final String KEYWORD_REDO = "redo";
	public static final String KEYWORD_DONE = "done";
	public static final String KEYWORD_UNDONE = "undone";
	public static final String KEYWORD_CLEAR = "clear";
	public static final String KEYWORD_EXPORT = "export";
	public static final String KEYWORD_ALL = "all";
	public static final String KEYWORD_EXIT = "exit";

	public static final String KEYWORD_DAILY = "daily";
	public static final String KEYWORD_WEEKLY = "weekly";
	public static final String KEYWORD_MONTHLY = "monthly";
	public static final String KEYWORD_YEARLY = "yearly";
	public static final String KEYWORD_DEFAULT = "default";
	public static final String KEYWORD_DEFAULT_OCCURENCE = KEYWORD_WEEKLY;

	public static final String KEYWORD_DISPLAY = "display";
	public static final String KEYWORD_DAY = "day";
	public static final String KEYWORD_DAYS = "days";
	public static final String KEYWORD_WEEK = "week";
	public static final String KEYWORD_WEEKS = "weeks";
	public static final String KEYWORD_MONTH = "month";
	public static final String KEYWORD_MONTHS = "months";
	public static final String KEYWORD_YEAR = "year";
	public static final String KEYWORD_YEARS = "years";
	public static final String KEYWORD_DATE = "date";

	public static final String DATE_MAX = "31 DECEMBER 9999";
	public static final String DATE_MIN = "1 JANUARY 1970";

	public final static String KEYWORD_DEADLINE_TASK = "deadline";
	public final static String KEYWORD_FLOATING_TASK = "floating";
	public final static String KEYWORD_NORMAL_TASK = "normal";
	public final static String KEYWORD_RECUR_TASK = "recur";

	public static final int PRIORITY_LOW = 0;
	public static final int PRIORITY_MEDIUM = 1;
	public static final int PRIORITY_HIGH = 2;
	public static final int PRIORITY_DEFAULT_PRIORITY = PRIORITY_MEDIUM;
	public static final int PRIORITY_MAX = PRIORITY_HIGH;
	public static final int PRIORITY_MIN = PRIORITY_LOW;

	/**
	 * This method will create the default keyword mapping, that is minimally
	 * required by the application.
	 * 
	 * @return the default mapping of keywords
	 */
	//@author A0111916M
	public static Map<String, String> createBasicMap() {

		Map<String, String> basicCommand = new HashMap<String, String>();
		basicCommand.put(KEYWORD_ADD, KEYWORD_ADD);
		basicCommand.put(KEYWORD_UPDATE, KEYWORD_UPDATE);
		basicCommand.put(KEYWORD_DELETE, KEYWORD_DELETE);
		basicCommand.put(KEYWORD_RETRIEVE, KEYWORD_RETRIEVE);
		basicCommand.put(KEYWORD_BY, KEYWORD_BY);
		basicCommand.put(KEYWORD_ON, KEYWORD_ON);
		basicCommand.put(KEYWORD_AT, KEYWORD_AT);
		basicCommand.put(KEYWORD_FROM, KEYWORD_FROM);
		basicCommand.put(KEYWORD_SINCE, KEYWORD_SINCE);
		basicCommand.put(KEYWORD_TO, KEYWORD_TO);
		basicCommand.put(KEYWORD_EVERY, KEYWORD_EVERY);
		basicCommand.put(KEYWORD_PRIORITY, KEYWORD_PRIORITY);
		basicCommand.put(KEYWORD_REDO, KEYWORD_REDO);
		basicCommand.put(KEYWORD_UNDO, KEYWORD_UNDO);
		basicCommand.put(KEYWORD_DONE, KEYWORD_DONE);
		basicCommand.put(KEYWORD_UNDONE, KEYWORD_UNDONE);
		basicCommand.put(KEYWORD_CLEAR, KEYWORD_CLEAR);
		basicCommand.put(KEYWORD_EXPORT, KEYWORD_EXPORT);
		basicCommand.put(KEYWORD_ALL, KEYWORD_ALL);
		basicCommand.put(KEYWORD_EXIT, KEYWORD_EXIT);
		basicCommand.put(KEYWORD_DAILY, KEYWORD_DAILY);
		basicCommand.put(KEYWORD_WEEKLY, KEYWORD_WEEKLY);
		basicCommand.put(KEYWORD_MONTHLY, KEYWORD_MONTHLY);
		basicCommand.put(KEYWORD_YEARLY, KEYWORD_YEARLY);
		basicCommand.put(KEYWORD_DISPLAY, KEYWORD_DISPLAY);
		basicCommand.put("high", Integer.toString(PRIORITY_HIGH));
		basicCommand.put("medium", Integer.toString(PRIORITY_MEDIUM));
		basicCommand.put("low", Integer.toString(PRIORITY_LOW));
		basicCommand.put("search", KEYWORD_RETRIEVE);

		return basicCommand;
	}
}
