package validator;

import java.util.List;
import java.util.Map;

import data.ConfigIO;

public class Validator {

	private Map<String, String> keywordFullMap = null;

	/**
	 * This constructor will load the configuration file.
	 * 
	 */
	// @author A0111916M
	public Validator() {

		loadConfigFile();
	}

	/**
	 * This method will load the configuration file named command.cfg. This
	 * configuration files contains the keyword mapping that is customized by
	 * the user, if any.
	 * 
	 */
	// @author A0111916M
	private void loadConfigFile() {

		ConfigIO config = new ConfigIO();
		keywordFullMap = config.getFullKeywordMap();
	}

	/**
	 * This method will validate if a single word is a keyword. It will return a
	 * boolean value of true, if it is a keyword (or customized keyword), as
	 * defined in Map<String, String>keywordFullMap. It will return false
	 * otherwise.
	 * 
	 * @param keyword
	 *            A word that is to be determined is a keyword
	 * @return boolean value
	 */
	// @author A0111916M
	public boolean validateKeyword(String keyword) {

		boolean isKeyword = false;

		fixKeywordInconsistency(keyword);

		if (keywordFullMap.containsKey(keyword)) {
			isKeyword = true;
		}

		return isKeyword;
	}

	/**
	 * This method will validate the list of keywords if it has a valid
	 * structure or sequence. This method will return true if the given list
	 * have a valid sequence of keywords, as specified in KeywordStructure. It
	 * will return false otherwise.
	 * 
	 * @param keywordList
	 *            A list of keywords sequence that is to be validated
	 * @return boolean value
	 */
	// @author A0111916M
	public boolean validateKeywordSequence(List<String> keywordList) {

		fixKeywordInconsistency(keywordList);

		KeywordStructure keySequence = new KeywordStructure();
		boolean isValidSequence = keySequence.checkKeyword(keywordList);

		return isValidSequence;
	}

	/**
	 * Fix the inconsistencies in a given keyword.
	 * 
	 * @param keyword
	 *            keyword to be fixed
	 * @return fixed keyword
	 */
	// @author A0111916M
	private String fixKeywordInconsistency(String keyword) {

		keyword = keyword.toLowerCase();
		keyword = keyword.trim();

		return keyword;
	}

	/**
	 * Fix the inconsistencies in a given list of keywords.
	 * 
	 * @param keywordList
	 *            list of keywords to be fixed
	 * @return fixed list of keywords
	 */
	// @author A0111916M
	private List<String> fixKeywordInconsistency(List<String> keywordList) {

		for (int i = 0; i < keywordList.size(); i++) {
			String currKeyWord = keywordList.get(i);
			currKeyWord = currKeyWord.toLowerCase();
			currKeyWord = currKeyWord.trim();
			keywordList.set(i, currKeyWord);
		}

		return keywordList;
	}

	/**
	 * Returns the keywordFullMap that had been retrieved before.
	 * 
	 * @return keywordFullMap the mapping of customized keywords
	 */
	// @author A0111916M
	public Map<String, String> getKeywordFullMap() {
		return keywordFullMap;
	}
}
