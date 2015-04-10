package validator;

import java.util.List;
import java.util.Map;

import data.ConfigIO;

public class Validator {

	private Map<String, String> keywordFullMap = null;

	public Validator() {

		loadConfigFile();
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	private void loadConfigFile() {

		ConfigIO config = new ConfigIO();
		keywordFullMap = config.getFullKeywordMap();
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public boolean validateKeyword(String keyword) {

		boolean isKeyword = false;

		fixKeywordInconsistency(keyword);

		if (keywordFullMap.containsKey(keyword)) {
			isKeyword = true;
		}

		return isKeyword;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public boolean validateKeywordSequence(List<String> keywordList) {

		fixKeywordInconsistency(keywordList);

		KeywordStructure keySequence = new KeywordStructure();
		boolean isValidSequence = keySequence.checkKeyword(keywordList);

		return isValidSequence;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	private String fixKeywordInconsistency(String keyword) {

		keyword = keyword.toLowerCase();
		keyword = keyword.trim();

		return keyword;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	private List<String> fixKeywordInconsistency(List<String> keywordList) {

		for (int i = 0; i < keywordList.size(); i++) {
			String currKeyWord = keywordList.get(i);
			currKeyWord = currKeyWord.toLowerCase();
			currKeyWord = currKeyWord.trim();
			keywordList.set(i, currKeyWord);
		}

		return keywordList;
	}

	public Map<String, String> getKeywordFullMap() {
		return keywordFullMap;
	}
}
