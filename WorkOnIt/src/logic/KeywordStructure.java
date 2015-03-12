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
		initRetrieveKeyword();
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

		KeywordNode keyToEvery = new KeywordNode(Validator.KEYWORD_TO);
		keyToEvery.addSubsequentKeywords(keyPriority);
		keyToEvery.addSubsequentKeywords(keyDaily);
		keyToEvery.addSubsequentKeywords(keyWeekly);
		keyToEvery.addSubsequentKeywords(keyMonthly);
		keyToEvery.addSubsequentKeywords(keyYearly);
		
		KeywordNode keyEvery = new KeywordNode(Validator.KEYWORD_EVERY);
		keyEvery.addSubsequentKeywords(keyPriority);
		keyEvery.addSubsequentKeywords(keyToEvery);
		keyEvery.addSubsequentKeywords(keyDaily);
		keyEvery.addSubsequentKeywords(keyWeekly);
		keyEvery.addSubsequentKeywords(keyMonthly);
		keyEvery.addSubsequentKeywords(keyYearly);
		
		KeywordNode keyBy = new KeywordNode(Validator.KEYWORD_BY);
		keyBy.addSubsequentKeywords(keyPriority);
		
		KeywordNode keyToOn = new KeywordNode(Validator.KEYWORD_TO);
		keyToOn.addSubsequentKeywords(keyPriority);
		
		KeywordNode keyFromOn = new KeywordNode(Validator.KEYWORD_FROM);
		keyFromOn.addSubsequentKeywords(keyToOn);
		
		KeywordNode keyOn = new KeywordNode(Validator.KEYWORD_ON);
		keyOn.addSubsequentKeywords(keyPriority);
		keyOn.addSubsequentKeywords(keyFromOn);
		keyOn.addSubsequentKeywords(keyToOn);

		KeywordNode keyOnFrom = new KeywordNode(Validator.KEYWORD_ON);
		keyOnFrom.addSubsequentKeywords(keyPriority);
		
		KeywordNode keyToFrom = new KeywordNode(Validator.KEYWORD_TO);
		keyToFrom.addSubsequentKeywords(keyPriority);
		keyToFrom.addSubsequentKeywords(keyOnFrom);
		
		KeywordNode keyFrom = new KeywordNode(Validator.KEYWORD_FROM);
		keyFrom.addSubsequentKeywords(keyPriority);
		keyFrom.addSubsequentKeywords(keyToFrom);
		
		KeywordNode keyAdd = new KeywordNode(Validator.KEYWORD_ADD);
		keyAdd.addSubsequentKeywords(keyPriority);
		keyAdd.addSubsequentKeywords(keyEvery);
		keyAdd.addSubsequentKeywords(keyBy);
		keyAdd.addSubsequentKeywords(keyOn);
		keyAdd.addSubsequentKeywords(keyFrom);
		
		mainKeywordList.add(keyAdd);
	}
	
	private void initRetrieveKeyword() {

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
