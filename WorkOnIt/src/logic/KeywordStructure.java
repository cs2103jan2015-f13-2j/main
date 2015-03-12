package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.ConfigIO;
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

		KeywordNode keyComplete = new KeywordNode(Validator.KEYWORD_COMPLETE);
		KeywordNode keyIncomplete = new KeywordNode(Validator.KEYWORD_INCOMPLETE);
		
		KeywordNode keyAll = new KeywordNode(Validator.KEYWORD_ALL);
		keyAll.addSubsequentKeywords(keyComplete);
		keyAll.addSubsequentKeywords(keyIncomplete);
		
		KeywordNode keyToBase = new KeywordNode(Validator.KEYWORD_TO);
		keyToBase.addSubsequentKeywords(keyComplete);
		keyToBase.addSubsequentKeywords(keyIncomplete);
		
		KeywordNode keyAtBase = new KeywordNode(Validator.KEYWORD_AT);
		keyAtBase.addSubsequentKeywords(keyComplete);
		keyAtBase.addSubsequentKeywords(keyIncomplete);
		
		KeywordNode keyFromBase = new KeywordNode(Validator.KEYWORD_FROM);
		keyFromBase.addSubsequentKeywords(keyToBase);
		keyFromBase.addSubsequentKeywords(keyComplete);
		keyFromBase.addSubsequentKeywords(keyIncomplete);
		
		KeywordNode keyAt = new KeywordNode(Validator.KEYWORD_AT);
		keyAt.addSubsequentKeywords(keyFromBase);
		keyAt.addSubsequentKeywords(keyComplete);
		keyAt.addSubsequentKeywords(keyIncomplete);
		
		KeywordNode keyOn = new KeywordNode(Validator.KEYWORD_ON);
		keyOn.addSubsequentKeywords(keyFromBase);
		keyOn.addSubsequentKeywords(keyAtBase);
		keyOn.addSubsequentKeywords(keyComplete);
		keyOn.addSubsequentKeywords(keyIncomplete);
		
		KeywordNode keyToFrom = new KeywordNode(Validator.KEYWORD_TO);
		keyToFrom.addSubsequentKeywords(keyAtBase);
		keyToFrom.addSubsequentKeywords(keyFromBase);
		keyToFrom.addSubsequentKeywords(keyComplete);
		keyToFrom.addSubsequentKeywords(keyIncomplete);
		
		KeywordNode keyFrom = new KeywordNode(Validator.KEYWORD_FROM);
		keyFrom.addSubsequentKeywords(keyAtBase);
		keyFrom.addSubsequentKeywords(keyFromBase);
		keyFrom.addSubsequentKeywords(keyToFrom);
		keyFrom.addSubsequentKeywords(keyComplete);
		keyFrom.addSubsequentKeywords(keyIncomplete);
		
		KeywordNode keyToPriority = new KeywordNode(Validator.KEYWORD_TO);
		keyToPriority.addSubsequentKeywords(keyAtBase);
		keyToPriority.addSubsequentKeywords(keyFromBase);
		keyToPriority.addSubsequentKeywords(keyComplete);
		keyToPriority.addSubsequentKeywords(keyIncomplete);
		
		KeywordNode keyFromPriority = new KeywordNode(Validator.KEYWORD_FROM);
		keyFromPriority.addSubsequentKeywords(keyAtBase);
		keyFromPriority.addSubsequentKeywords(keyFromBase);
		keyFromPriority.addSubsequentKeywords(keyToPriority);
		keyFromPriority.addSubsequentKeywords(keyComplete);
		keyFromPriority.addSubsequentKeywords(keyIncomplete);
		
		KeywordNode keyPriority = new KeywordNode(Validator.KEYWORD_PRIORITY);
		keyPriority.addSubsequentKeywords(keyFromPriority);
		keyPriority.addSubsequentKeywords(keyAtBase);
		keyPriority.addSubsequentKeywords(keyComplete);
		keyPriority.addSubsequentKeywords(keyIncomplete);
		
		KeywordNode keyRetrieve = new KeywordNode(Validator.KEYWORD_RETRIEVE);
		keyRetrieve.addSubsequentKeywords(keyAll);
		keyRetrieve.addSubsequentKeywords(keyAt);
		keyRetrieve.addSubsequentKeywords(keyOn);
		keyRetrieve.addSubsequentKeywords(keyFrom);
		keyRetrieve.addSubsequentKeywords(keyPriority);
		keyRetrieve.addSubsequentKeywords(keyToBase);
		keyRetrieve.addSubsequentKeywords(keyComplete);
		keyRetrieve.addSubsequentKeywords(keyIncomplete);
		
		mainKeywordList.add(keyRetrieve);
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
		Map<String, String> keywordFullMap = loadConfigFile();
		List<KeywordNode> currentKeywordList = mainKeywordList;

		for (int currIndex = 0; currIndex < keywords.size(); currIndex++) {

			String currentKeyword = keywords.get(currIndex);
			String resolvedWord = keywordFullMap.get(currentKeyword);

			if(resolvedWord != null) {
				try {
					Integer.parseInt(resolvedWord);
					isCorrectKeyword = true;
					break;
					
				} catch(NumberFormatException e) {
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
	
	private Map<String, String> loadConfigFile() {

		ConfigIO config = new ConfigIO();
		Map<String, String> keywordFullMap = config.getFullKeywordMap();
		
		return keywordFullMap;
	}
}
