package parser;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import resource.KeywordConstant;
import entity.Success;
import entity.SuccessDisplay;
import entity.Task;

public class AuxParser {

	private Map<String, String> keywordFullMap = null;
	private DataParser dataParser = null;

	public AuxParser(Map<String, String> keywordFullMap, DataParser dataParser) {
		this.keywordFullMap = keywordFullMap;
		this.dataParser = dataParser;
	}

	/**
	 * This secondary retrieval serves to retrieve the task list after previous
	 * operations have been executed. The retrieved list will be appended into
	 * the Success object and passed back to Main UI
	 *
	 * @param status
	 *            The main Success object without secondary list
	 */
	// @author A0111837
	protected void secondaryListRetrieval(Success status) {

		Success retrievalStatus = null;
		CommandParser commandParser = new CommandParser();

		if (status.isSuccess() == true && dataParser.getLastRetrieve() != null) {

			Scanner retrievalSD = new Scanner(dataParser.getLastRetrieve());
			String type = null;
			if (retrievalSD.hasNext()) {
				type = retrievalSD.next();
				type = keywordFullMap.get(type);

				if (type.equalsIgnoreCase(KeywordConstant.KEYWORD_DISPLAY)) {
					System.out.println(type);
					retrievalStatus = (SuccessDisplay) retrievalStatus;
				}
			}
			retrievalStatus = commandParser.parseCommand(dataParser
					.getLastRetrieve());

			if (retrievalStatus instanceof SuccessDisplay) {
				status.setObj(retrievalStatus);
				dataParser
						.setRetrievedTaskList((ArrayList<Task>) retrievalStatus
								.getObj());
			} else {
				status.setObj(retrievalStatus.getObj());
				dataParser
						.setRetrievedTaskList((ArrayList<Task>) retrievalStatus
								.getObj());
			}
			retrievalSD.close();
		}
	}

	/**
	 * Returns a boolean regarding whether the String parameter is a number or
	 * not.
	 *
	 * @param text
	 *            String to be determined whether it is a number
	 * @return boolean whether text is a number
	 */
	// @author A0111837J
	public boolean isNaN(String text) {
		boolean isNumber;

		try {
			Integer.parseInt(text);
			isNumber = true;
		} catch (NumberFormatException e) {
			isNumber = false;
		}

		return !isNumber;
	}
}
