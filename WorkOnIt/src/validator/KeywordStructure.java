package validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import data.ConfigIO;
import resource.KeywordConstant;
import entity.KeywordNode;

public class KeywordStructure {

	private List<KeywordNode> mainKeywordList;
	private static final Logger LOGGER = Logger
			.getLogger(KeywordStructure.class.getName());

	/**
	 * This constructor will execute the init method. By instantiating this
	 * class, it creates a keyword sequence for the respective commands. This
	 * will allow the validation of proper command. All keywords are structured
	 * in a form of Linked List.
	 * 
	 * @return
	 */
	//@author A0111916M
	public KeywordStructure() {

		init();
	}

	/**
	 * It will execute all the possible keyword configurations.
	 */

	private void init() {

		mainKeywordList = new ArrayList<KeywordNode>();

		initAddKeyword();
		initRetrieveKeyword();
		initUpdateKeyword();
		initDeleteKeyword();
		initDoneKeyword();
		initUndoneKeyword();
		initRedoKeyword();
		initUndoKeyword();
		initClearKeyword();
		initDisplayKeyword();
		initExitKeyword();

		LOGGER.fine("Keyword Structure initialized fully");
	}

	/**
	 * It will execute the keyword configuration for add command. This method
	 * will arrange the possible combination and sequence for this command.
	 */

	private void initAddKeyword() {

		KeywordNode keyPriority = new KeywordNode(
				KeywordConstant.KEYWORD_PRIORITY);

		KeywordNode keyDaily = new KeywordNode(KeywordConstant.KEYWORD_DAILY);
		keyDaily.addSubsequentKeywords(keyPriority);
		KeywordNode keyWeekly = new KeywordNode(KeywordConstant.KEYWORD_WEEKLY);
		keyWeekly.addSubsequentKeywords(keyPriority);
		KeywordNode keyMonthly = new KeywordNode(
				KeywordConstant.KEYWORD_MONTHLY);
		keyMonthly.addSubsequentKeywords(keyPriority);
		KeywordNode keyYearly = new KeywordNode(KeywordConstant.KEYWORD_YEARLY);
		keyYearly.addSubsequentKeywords(keyPriority);

		KeywordNode keyToEvery = new KeywordNode(KeywordConstant.KEYWORD_TO);
		keyToEvery.addSubsequentKeywords(keyPriority);
		keyToEvery.addSubsequentKeywords(keyDaily);
		keyToEvery.addSubsequentKeywords(keyWeekly);
		keyToEvery.addSubsequentKeywords(keyMonthly);
		keyToEvery.addSubsequentKeywords(keyYearly);

		KeywordNode keyEvery = new KeywordNode(KeywordConstant.KEYWORD_EVERY);
		keyEvery.addSubsequentKeywords(keyPriority);
		keyEvery.addSubsequentKeywords(keyToEvery);
		keyEvery.addSubsequentKeywords(keyDaily);
		keyEvery.addSubsequentKeywords(keyWeekly);
		keyEvery.addSubsequentKeywords(keyMonthly);
		keyEvery.addSubsequentKeywords(keyYearly);

		KeywordNode keyBy = new KeywordNode(KeywordConstant.KEYWORD_BY);
		keyBy.addSubsequentKeywords(keyPriority);

		KeywordNode keyToOn = new KeywordNode(KeywordConstant.KEYWORD_TO);
		keyToOn.addSubsequentKeywords(keyPriority);

		KeywordNode keyFromOn = new KeywordNode(KeywordConstant.KEYWORD_FROM);
		keyFromOn.addSubsequentKeywords(keyToOn);

		KeywordNode keyOn = new KeywordNode(KeywordConstant.KEYWORD_ON);
		keyOn.addSubsequentKeywords(keyPriority);
		keyOn.addSubsequentKeywords(keyFromOn);
		keyOn.addSubsequentKeywords(keyToOn);

		KeywordNode keyAt = new KeywordNode(KeywordConstant.KEYWORD_AT);
		keyAt.addSubsequentKeywords(keyPriority);
		keyAt.addSubsequentKeywords(keyFromOn);
		keyAt.addSubsequentKeywords(keyToOn);

		KeywordNode keyOnFrom = new KeywordNode(KeywordConstant.KEYWORD_ON);
		keyOnFrom.addSubsequentKeywords(keyPriority);

		KeywordNode keyToFrom = new KeywordNode(KeywordConstant.KEYWORD_TO);
		keyToFrom.addSubsequentKeywords(keyPriority);
		keyToFrom.addSubsequentKeywords(keyOnFrom);

		KeywordNode keyFrom = new KeywordNode(KeywordConstant.KEYWORD_FROM);
		keyFrom.addSubsequentKeywords(keyPriority);
		keyFrom.addSubsequentKeywords(keyToFrom);

		KeywordNode keyAdd = new KeywordNode(KeywordConstant.KEYWORD_ADD);
		keyAdd.addSubsequentKeywords(keyPriority);
		keyAdd.addSubsequentKeywords(keyEvery);
		keyAdd.addSubsequentKeywords(keyBy);
		keyAdd.addSubsequentKeywords(keyOn);
		keyAdd.addSubsequentKeywords(keyFrom);
		keyAdd.addSubsequentKeywords(keyAt);

		mainKeywordList.add(keyAdd);
	}

	/**
	 * It will execute the keyword configuration for retrieve command. This
	 * method will arrange the possible combination and sequence for this
	 * command.
	 */

	private void initRetrieveKeyword() {

		KeywordNode keyDone = new KeywordNode(KeywordConstant.KEYWORD_DONE);
		KeywordNode keyUndone = new KeywordNode(KeywordConstant.KEYWORD_UNDONE);

		KeywordNode keyAll = new KeywordNode(KeywordConstant.KEYWORD_ALL);
		keyAll.addSubsequentKeywords(keyDone);
		keyAll.addSubsequentKeywords(keyUndone);

		KeywordNode keyToBase = new KeywordNode(KeywordConstant.KEYWORD_TO);
		keyToBase.addSubsequentKeywords(keyDone);
		keyToBase.addSubsequentKeywords(keyUndone);

		KeywordNode keyAtBase = new KeywordNode(KeywordConstant.KEYWORD_AT);
		keyAtBase.addSubsequentKeywords(keyDone);
		keyAtBase.addSubsequentKeywords(keyUndone);

		KeywordNode keyFromBase = new KeywordNode(KeywordConstant.KEYWORD_FROM);
		keyFromBase.addSubsequentKeywords(keyToBase);
		keyFromBase.addSubsequentKeywords(keyDone);
		keyFromBase.addSubsequentKeywords(keyUndone);

		KeywordNode keyAt = new KeywordNode(KeywordConstant.KEYWORD_AT);
		keyAt.addSubsequentKeywords(keyFromBase);
		keyAt.addSubsequentKeywords(keyDone);
		keyAt.addSubsequentKeywords(keyUndone);

		KeywordNode keyOn = new KeywordNode(KeywordConstant.KEYWORD_ON);
		keyOn.addSubsequentKeywords(keyFromBase);
		keyOn.addSubsequentKeywords(keyAtBase);
		keyOn.addSubsequentKeywords(keyDone);
		keyOn.addSubsequentKeywords(keyUndone);

		KeywordNode keyToFrom = new KeywordNode(KeywordConstant.KEYWORD_TO);
		keyToFrom.addSubsequentKeywords(keyAtBase);
		keyToFrom.addSubsequentKeywords(keyFromBase);
		keyToFrom.addSubsequentKeywords(keyDone);
		keyToFrom.addSubsequentKeywords(keyUndone);

		KeywordNode keyFrom = new KeywordNode(KeywordConstant.KEYWORD_FROM);
		keyFrom.addSubsequentKeywords(keyAtBase);
		keyFrom.addSubsequentKeywords(keyFromBase);
		keyFrom.addSubsequentKeywords(keyToFrom);
		keyFrom.addSubsequentKeywords(keyDone);
		keyFrom.addSubsequentKeywords(keyUndone);

		KeywordNode keyToPriority = new KeywordNode(KeywordConstant.KEYWORD_TO);
		keyToPriority.addSubsequentKeywords(keyAtBase);
		keyToPriority.addSubsequentKeywords(keyFromBase);
		keyToPriority.addSubsequentKeywords(keyDone);
		keyToPriority.addSubsequentKeywords(keyUndone);

		KeywordNode keyFromPriority = new KeywordNode(
				KeywordConstant.KEYWORD_FROM);
		keyFromPriority.addSubsequentKeywords(keyAtBase);
		keyFromPriority.addSubsequentKeywords(keyFromBase);
		keyFromPriority.addSubsequentKeywords(keyToPriority);
		keyFromPriority.addSubsequentKeywords(keyDone);
		keyFromPriority.addSubsequentKeywords(keyUndone);

		KeywordNode keyPriority = new KeywordNode(
				KeywordConstant.KEYWORD_PRIORITY);
		keyPriority.addSubsequentKeywords(keyFromPriority);
		keyPriority.addSubsequentKeywords(keyAtBase);
		keyPriority.addSubsequentKeywords(keyDone);
		keyPriority.addSubsequentKeywords(keyUndone);

		KeywordNode keyRetrieve = new KeywordNode(
				KeywordConstant.KEYWORD_RETRIEVE);
		keyRetrieve.addSubsequentKeywords(keyAll);
		keyRetrieve.addSubsequentKeywords(keyAt);
		keyRetrieve.addSubsequentKeywords(keyOn);
		keyRetrieve.addSubsequentKeywords(keyFrom);
		keyRetrieve.addSubsequentKeywords(keyPriority);
		keyRetrieve.addSubsequentKeywords(keyToBase);
		keyRetrieve.addSubsequentKeywords(keyDone);
		keyRetrieve.addSubsequentKeywords(keyUndone);

		mainKeywordList.add(keyRetrieve);
	}

	/**
	 * It will execute the keyword configuration for update command. This method
	 * will arrange the possible combination and sequence for this command.
	 */

	private void initUpdateKeyword() {

		KeywordNode keyPriority = new KeywordNode(
				KeywordConstant.KEYWORD_PRIORITY);

		KeywordNode keyDaily = new KeywordNode(KeywordConstant.KEYWORD_DAILY);
		keyDaily.addSubsequentKeywords(keyPriority);
		KeywordNode keyWeekly = new KeywordNode(KeywordConstant.KEYWORD_WEEKLY);
		keyWeekly.addSubsequentKeywords(keyPriority);
		KeywordNode keyMonthly = new KeywordNode(
				KeywordConstant.KEYWORD_MONTHLY);
		keyMonthly.addSubsequentKeywords(keyPriority);
		KeywordNode keyYearly = new KeywordNode(KeywordConstant.KEYWORD_YEARLY);
		keyYearly.addSubsequentKeywords(keyPriority);

		KeywordNode keyToEvery = new KeywordNode(KeywordConstant.KEYWORD_TO);
		keyToEvery.addSubsequentKeywords(keyPriority);
		keyToEvery.addSubsequentKeywords(keyDaily);
		keyToEvery.addSubsequentKeywords(keyWeekly);
		keyToEvery.addSubsequentKeywords(keyMonthly);
		keyToEvery.addSubsequentKeywords(keyYearly);

		KeywordNode keyEvery = new KeywordNode(KeywordConstant.KEYWORD_EVERY);
		keyEvery.addSubsequentKeywords(keyPriority);
		keyEvery.addSubsequentKeywords(keyToEvery);
		keyEvery.addSubsequentKeywords(keyDaily);
		keyEvery.addSubsequentKeywords(keyWeekly);
		keyEvery.addSubsequentKeywords(keyMonthly);
		keyEvery.addSubsequentKeywords(keyYearly);

		KeywordNode keyBy = new KeywordNode(KeywordConstant.KEYWORD_BY);
		keyBy.addSubsequentKeywords(keyPriority);

		KeywordNode keyToOn = new KeywordNode(KeywordConstant.KEYWORD_TO);
		keyToOn.addSubsequentKeywords(keyPriority);

		KeywordNode keyFromOn = new KeywordNode(KeywordConstant.KEYWORD_FROM);
		keyFromOn.addSubsequentKeywords(keyToOn);

		KeywordNode keyOn = new KeywordNode(KeywordConstant.KEYWORD_ON);
		keyOn.addSubsequentKeywords(keyPriority);
		keyOn.addSubsequentKeywords(keyFromOn);
		keyOn.addSubsequentKeywords(keyToOn);

		KeywordNode keyAt = new KeywordNode(KeywordConstant.KEYWORD_AT);
		keyAt.addSubsequentKeywords(keyPriority);
		keyAt.addSubsequentKeywords(keyFromOn);
		keyAt.addSubsequentKeywords(keyToOn);

		KeywordNode keyOnFrom = new KeywordNode(KeywordConstant.KEYWORD_ON);
		keyOnFrom.addSubsequentKeywords(keyPriority);

		KeywordNode keyToFrom = new KeywordNode(KeywordConstant.KEYWORD_TO);
		keyToFrom.addSubsequentKeywords(keyPriority);
		keyToFrom.addSubsequentKeywords(keyOnFrom);

		KeywordNode keyFrom = new KeywordNode(KeywordConstant.KEYWORD_FROM);
		keyFrom.addSubsequentKeywords(keyPriority);
		keyFrom.addSubsequentKeywords(keyToFrom);

		KeywordNode keyUpdate = new KeywordNode(KeywordConstant.KEYWORD_UPDATE);
		keyUpdate.addSubsequentKeywords(keyPriority);
		keyUpdate.addSubsequentKeywords(keyEvery);
		keyUpdate.addSubsequentKeywords(keyBy);
		keyUpdate.addSubsequentKeywords(keyOn);
		keyUpdate.addSubsequentKeywords(keyFrom);
		keyUpdate.addSubsequentKeywords(keyAt);

		mainKeywordList.add(keyUpdate);
	}

	/**
	 * It will execute the keyword configuration for delete command. This method
	 * will arrange the possible combination and sequence for this command.
	 */

	private void initDeleteKeyword() {

		KeywordNode keyDel = new KeywordNode(KeywordConstant.KEYWORD_DELETE);

		mainKeywordList.add(keyDel);
	}

	/**
	 * It will execute the keyword configuration for undo command. This method
	 * will arrange the possible combination and sequence for this command.
	 */

	private void initUndoKeyword() {
		KeywordNode keyUndo = new KeywordNode(KeywordConstant.KEYWORD_UNDO);

		mainKeywordList.add(keyUndo);
	}

	/**
	 * It will execute the keyword configuration for redo command. This method
	 * will arrange the possible combination and sequence for this command.
	 */

	private void initRedoKeyword() {
		KeywordNode keyRedo = new KeywordNode(KeywordConstant.KEYWORD_REDO);

		mainKeywordList.add(keyRedo);
	}

	/**
	 * It will execute the keyword configuration for done command. This method
	 * will arrange the possible combination and sequence for this command.
	 */

	private void initDoneKeyword() {
		KeywordNode keyDone = new KeywordNode(KeywordConstant.KEYWORD_DONE);

		mainKeywordList.add(keyDone);
	}

	/**
	 * It will execute the keyword configuration for undone command. This method
	 * will arrange the possible combination and sequence for this command.
	 */

	private void initUndoneKeyword() {
		KeywordNode keyUndone = new KeywordNode(KeywordConstant.KEYWORD_UNDONE);

		mainKeywordList.add(keyUndone);
	}

	/**
	 * It will execute the keyword configuration for clear command. This method
	 * will arrange the possible combination and sequence for this command.
	 */

	private void initClearKeyword() {
		KeywordNode keyClear = new KeywordNode(KeywordConstant.KEYWORD_CLEAR);

		mainKeywordList.add(keyClear);
	}

	/**
	 * It will execute the keyword configuration for display command. This
	 * method will arrange the possible combination and sequence for this
	 * command.
	 */

	private void initDisplayKeyword() {
		KeywordNode keyDisplay = new KeywordNode(
				KeywordConstant.KEYWORD_DISPLAY);

		mainKeywordList.add(keyDisplay);
	}

	/**
	 * It will execute the keyword configuration for exit command. This method
	 * will arrange the possible combination and sequence for this command.
	 */

	private void initExitKeyword() {
		KeywordNode keyExit = new KeywordNode(KeywordConstant.KEYWORD_EXIT);

		mainKeywordList.add(keyExit);
	}

	/**
	 * This method will check the keyword sequence in the input parameter. It
	 * will return a boolean value of true, if the given keywords list is in a
	 * correct and expected order, as defined in the init methods above. It will
	 * return false otherwise.
	 * 
	 * @param keywords
	 *            List of keywords, in expected sequence
	 * @return boolean value
	 */

	public boolean checkKeyword(List<String> keywords) {

		boolean isCorrectKeyword = false;
		Map<String, String> keywordFullMap = loadConfigFile();
		List<KeywordNode> currentKeywordList = mainKeywordList;

		for (int currIndex = 0; currIndex < keywords.size(); currIndex++) {

			String currentKeyword = keywords.get(currIndex);
			String resolvedWord = keywordFullMap.get(currentKeyword);

			if (resolvedWord != null) {
				try {
					Integer.parseInt(resolvedWord);
					isCorrectKeyword = true;
					break;

				} catch (NumberFormatException e) {
					for (int i = 0; i < currentKeywordList.size(); i++) {

						KeywordNode node = currentKeywordList.get(i);

						if (node.equals(resolvedWord)) {
							isCorrectKeyword = true;
							currentKeywordList = node.getSubsequentKeywords();
							break;
						}
					}
				}

				int lastIndex = keywords.size() - 1;

				if (isCorrectKeyword && currIndex != lastIndex) {
					isCorrectKeyword = false;
				} else {
					break;
				}
			} else {
				isCorrectKeyword = false;
			}
		}

		return isCorrectKeyword;
	}

	/**
	 * This method will load the configuration file named command.cfg. This file
	 * is customizable by the user. User may enter their desired keyword
	 * mappings, according to their preference. Example: "create":"add" which
	 * means that the new keyword "create" is being mapped onto existing keyword
	 * "add".
	 * 
	 * @return Map<String, String> keywordFullMap The mapping of customized
	 *         keywords, according to user preference.
	 */

	private Map<String, String> loadConfigFile() {

		ConfigIO config = new ConfigIO();
		Map<String, String> keywordFullMap = config.getFullKeywordMap();

		return keywordFullMap;
	}
}
