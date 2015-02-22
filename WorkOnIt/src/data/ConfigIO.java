package data;

import java.util.HashMap;
import java.util.Map;

public class ConfigIO {
	
	private static final String KEYWORD_ADD = "add";
	private static final String KEYWORD_UPDATE = "update";
	private static final String KEYWORD_DELETE = "delete";
	private static final String KEYWORD_BY = "by";
	private static final String KEYWORD_FROM = "from";
	private static final String KEYWORD_TO = "to";
	private static final String KEYWORD_EVERY = "every";
	private static final String KEYWORD_UNTIL = "until";
	private static final String KEYWORD_PRIORITY = "priority";
	
	private static final int PRIORITY_LOW = 0;
	private static final int PRIORITY_MEDIUM = 1;
	private static final int PRIORITY_HIGH = 2;

	public Map<String, String> getFullKeywordMap() {
		
		Map<String, String> KEYWORD_MAP = new HashMap<String, String>();
		
		KEYWORD_MAP.put(KEYWORD_ADD, KEYWORD_ADD);
		KEYWORD_MAP.put(KEYWORD_UPDATE, KEYWORD_UPDATE);
		KEYWORD_MAP.put(KEYWORD_DELETE, KEYWORD_DELETE);
		KEYWORD_MAP.put(KEYWORD_BY, KEYWORD_BY);
		KEYWORD_MAP.put(KEYWORD_FROM, KEYWORD_FROM);
		KEYWORD_MAP.put(KEYWORD_TO, KEYWORD_TO);
		KEYWORD_MAP.put(KEYWORD_EVERY, KEYWORD_EVERY);
		KEYWORD_MAP.put(KEYWORD_UNTIL, KEYWORD_UNTIL);
		KEYWORD_MAP.put(KEYWORD_PRIORITY, KEYWORD_PRIORITY);
		
		return KEYWORD_MAP;
	}
}
