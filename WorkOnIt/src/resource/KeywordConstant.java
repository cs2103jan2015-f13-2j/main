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
	public static final String KEYWORD_COMPLETE = "complete";
	public static final String KEYWORD_INCOMPLETE = "incomplete";
	public static final String KEYWORD_CLEAR = "clear";
	public static final String KEYWORD_EXPORT = "export";
	public static final String KEYWORD_ALL = "all";
	public static final String KEYWORD_DAILY = "daily";
	public static final String KEYWORD_WEEKLY = "weekly";
	public static final String KEYWORD_MONTHLY = "monthly";
	public static final String KEYWORD_YEARLY = "yearly";
	public static final String KEYWORD_DEFAULT_OCCURENCE = KEYWORD_WEEKLY;

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
	
	private Map<String, String> basicCommand = new HashMap<String, String>();
	//create basic HashMap of keywords
}
