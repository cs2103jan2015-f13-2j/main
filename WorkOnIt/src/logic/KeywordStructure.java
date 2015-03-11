package logic;

import java.util.ArrayList;
import java.util.List;

import entity.KeywordNode;

public class KeywordStructure {

	private List<KeywordNode> mainKeywordList;

	public KeywordStructure() {

		init();
	}

	private void init() {

		mainKeywordList = new ArrayList<KeywordNode>();

		initAddKeyword();
		// initRetrieveKyword();
		// initUpdateKeyword();
		// initDeleteKeyword();
		// initExportKeyword();
		initUndoKeyword();
		initClearKeyword();
	}

	private void initAddKeyword() {
		KeywordNode keyPriority = new KeywordNode(Validator.KEYWORD_PRIORITY);

		KeywordNode keyDaily = new KeywordNode(Validator.KEYWORD_DAILY);
		keyDaily.addSubsequentKeywords(keyPriority);
		KeywordNode keyWeekly = new KeywordNode(Validator.KEYWORD_WEEKLY);
		keyWeekly.addSubsequentKeywords(keyPriority);
		KeywordNode keyMonthly = new KeywordNode(Validator.KEYWORD_MONTHLY);
		keyMonthly.addSubsequentKeywords(keyPriority);
		KeywordNode keyYearly = new KeywordNode(Validator.KEYWORD_YEARLY);
		keyYearly.addSubsequentKeywords(keyPriority);

		KeywordNode keyToNormal = new KeywordNode(Validator.KEYWORD_TO);
		keyToNormal.addSubsequentKeywords(keyPriority);

		KeywordNode keyToRecur = new KeywordNode(Validator.KEYWORD_TO);
		keyToRecur.addSubsequentKeywords(keyPriority);
		keyToRecur.addSubsequentKeywords(keyDaily);
		keyToRecur.addSubsequentKeywords(keyWeekly);
		keyToRecur.addSubsequentKeywords(keyMonthly);
		keyToRecur.addSubsequentKeywords(keyYearly);

		KeywordNode keyFrom = new KeywordNode(Validator.KEYWORD_FROM);
		keyFrom.addSubsequentKeywords(keyPriority);
		keyFrom.addSubsequentKeywords(keyToNormal);

		KeywordNode keyOn = new KeywordNode(Validator.KEYWORD_ON);
		keyOn.addSubsequentKeywords(keyPriority);
		keyOn.addSubsequentKeywords(keyToNormal);

		KeywordNode keyBy = new KeywordNode(Validator.KEYWORD_BY);
		keyBy.addSubsequentKeywords(keyPriority);

		KeywordNode keyEvery = new KeywordNode(Validator.KEYWORD_EVERY);
		keyEvery.addSubsequentKeywords(keyPriority);
		keyEvery.addSubsequentKeywords(keyToRecur);
		keyEvery.addSubsequentKeywords(keyDaily);
		keyEvery.addSubsequentKeywords(keyWeekly);
		keyEvery.addSubsequentKeywords(keyMonthly);
		keyEvery.addSubsequentKeywords(keyYearly);

		KeywordNode keyAdd = new KeywordNode(Validator.KEYWORD_ADD);
		keyAdd.addSubsequentKeywords(keyFrom);
		keyAdd.addSubsequentKeywords(keyOn);
		keyAdd.addSubsequentKeywords(keyBy);
		keyAdd.addSubsequentKeywords(keyEvery);

		mainKeywordList.add(keyAdd);
	}

	private void initUndoKeyword() {
		KeywordNode keyUndo = new KeywordNode(Validator.KEYWORD_UNDO);

		mainKeywordList.add(keyUndo);
	}

	private void initClearKeyword() {
		KeywordNode keyClear = new KeywordNode(Validator.KEYWORD_CLEAR);

		mainKeywordList.add(keyClear);
	}

	public boolean checkKeyword(List<String> keywords) {

		boolean isCorrectKeyword = false;
		List<KeywordNode> currentKeywordList = mainKeywordList;

		for (int currIndex = 0; currIndex < keywords.size(); currIndex++) {

			String currentKeyword = keywords.get(currIndex);

			for (int i = 0; i < currentKeywordList.size(); i++) {

				KeywordNode node = currentKeywordList.get(i);

				if (node.equals(currentKeyword)) {
					isCorrectKeyword = true;
					currentKeywordList = node.getSubsequentKeywords();
					break;
				}
			}

			int lastIndex = keywords.size() - 1;

			if (isCorrectKeyword && currIndex != lastIndex) {
				isCorrectKeyword = false;
			} else {
				break;
			}
		}

		return isCorrectKeyword;
	}
}
