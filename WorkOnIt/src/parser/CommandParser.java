package parser;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import logic.Engine;
import data.ConfigIO;
import resource.KeywordConstant;
import resource.Message;
import validator.Validator;
import entity.Task;
import entity.Success;

public class CommandParser {

	private Engine engine;
	private Map<String, String> keywordFullMap = null;
	private static DataParser dataParser = null;

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public CommandParser() {

		engine = new Engine();

		Validator keywordValidator = new Validator();
		keywordFullMap = keywordValidator.getKeywordFullMap();
		dataParser = new DataParser();
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
	public Success getHistory() {

		Success status = null;

		status = engine.getHistory();

		return status;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	public Success parseCommand(String fullCommand) {

		Success status = null;

		fullCommand = fullCommand.toLowerCase();
		Scanner sc = new Scanner(fullCommand);
		String commandInput = sc.next();
		String commandResolved = keywordFullMap.get(commandInput);

		if (commandResolved != null && sc.hasNext()) {

			String remainingCommand = sc.nextLine();

			if (commandResolved.equalsIgnoreCase(KeywordConstant.KEYWORD_ADD)) {

				status = executeAddCommand(remainingCommand);

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UPDATE)) {

				status = executeUpdateCommand(remainingCommand);

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DELETE)) {

				status = executeDeleteCommand(remainingCommand);

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_RETRIEVE)) {

				status = executeRetrieveCommand(remainingCommand);

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DISPLAY)) {

				status = executeDisplayCommand(remainingCommand);

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DONE)) {

				status = executeDoneCommand(remainingCommand);

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UNDONE)) {

				status = executeUndoneCommand(remainingCommand);

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UNDO)) {

				status = executeUndoCommand();

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_REDO)) {

				status = executeRedoCommand();

			} else if (commandResolved
					.equalsIgnoreCase(KeywordConstant.KEYWORD_EXIT)) {

				executeExitCommand();

			} else {
				status = new Success(false, Message.FAIL_PARSE_COMMAND);
			}

		} else {
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		}

		sc.close();

		return status;
	}

	private void executeExitCommand() {
		System.exit(0);
	}

	private Success executeUndoneCommand(String remainingCommand) {

		Success status;
		DoneParser undoneParser = new DoneParser(keywordFullMap, dataParser);

		remainingCommand = remainingCommand.trim();

		status = undoneParser.undoneCommand(remainingCommand);
		return status;
	}

	private Success executeDoneCommand(String remainingCommand) {

		Success status;
		DoneParser doneParser = new DoneParser(keywordFullMap, dataParser);

		remainingCommand = remainingCommand.trim();

		status = doneParser.doneCommand(remainingCommand);
		return status;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	private Success executeUndoCommand() {
		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
		Success status = engine.undoTask();
		auxParser.secondaryListRetrieval(status);

		return status;
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	private Success executeRedoCommand() {
		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
		Success status = engine.redoTask();
		auxParser.secondaryListRetrieval(status);

		return status;
	}

	private Success executeDisplayCommand(String remainingCommand) {

		Success status;
		DisplayParser displayParser = new DisplayParser();

		dataParser.setLastRetrieve(KeywordConstant.KEYWORD_DISPLAY);
		remainingCommand = remainingCommand.trim();

		status = displayParser.parseDisplayCommand(remainingCommand);

		dataParser.setRetrievedTaskList(null);
		if (status.isSuccess() == true) {
			dataParser.appendLastRetrieve(remainingCommand);
			if (status.getObj() != null) {
				dataParser.setRetrievedTaskList((ArrayList<Task>) status.getObj());
			}
		}
		return status;
	}

	private Success executeRetrieveCommand(String remainingCommand) {

		Success status;
		RetrieveParser retrieveParser = new RetrieveParser(keywordFullMap, dataParser);

		dataParser.setLastRetrieve(KeywordConstant.KEYWORD_RETRIEVE);
		remainingCommand = remainingCommand.trim();

		status = retrieveParser.parseRetrieveCommand(remainingCommand);

		dataParser.setRetrievedTaskList(null);
		if (status.isSuccess() == true) {
			dataParser.appendLastRetrieve(remainingCommand);
			if (status.getObj() != null) {
				dataParser.setRetrievedTaskList((ArrayList<Task>) status.getObj());
			}
		}
		return status;
	}

	private Success executeDeleteCommand(String remainingCommand) {

		Success status;
		DeleteParser deleteParser = new DeleteParser(keywordFullMap, dataParser);
		remainingCommand = remainingCommand.trim();

		status = deleteParser.parseDeleteCommand(remainingCommand);

		return status;
	}

	private Success executeUpdateCommand(String remainingCommand) {

		Success status;
		UpdateParser updateParser = new UpdateParser(keywordFullMap, dataParser);

		remainingCommand = remainingCommand.trim();

		status = updateParser.parseUpdateCommand(remainingCommand);

		return status;
	}

	private Success executeAddCommand(String remainingCommand) {

		Success status;
		AddParser addParser = new AddParser(keywordFullMap, dataParser);
		remainingCommand = remainingCommand.trim();

		status = addParser.processAddCommand(remainingCommand, dataParser);

		return status;
	}
}