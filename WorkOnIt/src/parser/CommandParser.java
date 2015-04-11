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
	 * Constructor for Command Parser. It will set up necessary datas.
	 */
	// @author A0111916M
	public CommandParser() {

		engine = new Engine();

		Validator keywordValidator = new Validator();
		keywordFullMap = keywordValidator.getKeywordFullMap();
		dataParser = new DataParser();
	}

	/**
	 * Load the configuration file (command.cfg) which contains the customized
	 * keyword mapping. The content of this file is in JSON.
	 */
	// @author A0111916M
	private void loadConfigFile() {

		ConfigIO config = new ConfigIO();
		keywordFullMap = config.getFullKeywordMap();
	}

	/**
	 * Get a list of history that were saved as a file, based on what the user
	 * have added.
	 * 
	 * @return Success object
	 */
	// @author A0111916M
	public Success getHistory() {

		Success status = null;

		status = engine.getHistory();

		return status;
	}

	/**
	 * The start of the parsing process. It will check the first word of
	 * fullCommand, and determine which command to execute.
	 * 
	 * @param fullCommand
	 *            Full command that is entered by user
	 * @return Success object
	 */
	// @author A0111916M
	public Success parseCommand(String fullCommand) {

		Success status = null;

		fullCommand = fullCommand.toLowerCase();
		Scanner sc = new Scanner(fullCommand);
		String commandInput = sc.next();
		String commandResolved = keywordFullMap.get(commandInput);
		String remainingCommand = null;

		if (commandResolved != null) {

			if (sc.hasNext()) {
				remainingCommand = sc.nextLine();
			}

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

	/**
	 * Exit the program if an exit command is entered.
	 */
	// @author A0111916M
	private void executeExitCommand() {
		System.exit(0);
	}

	/**
	 * Begin executing "undone" command. It will call DoneParser to continue
	 * parsing command for "undone". It will pass back a Success object to
	 * parseCommand method, which will then pass back to UI tier.
	 * 
	 * @param remainingCommand
	 *            The remaining command that were truncated
	 * @return Success object
	 */
	// @author A0111916M
	private Success executeUndoneCommand(String remainingCommand) {

		Success status;

		if (remainingCommand == null) {
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		} else {

			DoneParser undoneParser = new DoneParser(keywordFullMap, dataParser);

			remainingCommand = remainingCommand.trim();

			status = undoneParser.undoneCommand(remainingCommand);
		}

		return status;
	}

	/**
	 * Begin executing "done" command. It will call DoneParser to continue
	 * parsing command for "done". It will pass back a Success object to
	 * parseCommand method, which will then pass back to UI tier.
	 * 
	 * @param remainingCommand
	 *            The remaining command that were truncated
	 * @return Success object
	 */
	// @author A0111916M
	private Success executeDoneCommand(String remainingCommand) {

		Success status;

		if (remainingCommand == null) {
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		} else {

			DoneParser doneParser = new DoneParser(keywordFullMap, dataParser);

			remainingCommand = remainingCommand.trim();

			status = doneParser.doneCommand(remainingCommand);
		}

		return status;
	}

	/**
	 * Begin executing "undo" command. It will call Engine to continue parsing
	 * command for "undo". It will pass back a Success object to parseCommand
	 * method, which will then pass back to UI tier.
	 * 
	 * @return Success object
	 */
	// @author A0111916M
	private Success executeUndoCommand() {
		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
		Success status = engine.undoTask();
		auxParser.secondaryListRetrieval(status);

		return status;
	}

	/**
	 * Begin executing "redo" command. It will call Engine to continue parsing
	 * command for "redo". It will pass back a Success object to parseCommand
	 * method, which will then pass back to UI tier.
	 * 
	 * @return Success object
	 */
	// @author A0111916M
	private Success executeRedoCommand() {
		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
		Success status = engine.redoTask();
		auxParser.secondaryListRetrieval(status);

		return status;
	}

	/**
	 * Begin executing "display" command. It will call DisplayParser to continue
	 * parsing command for "display". It will pass back a Success object to
	 * parseCommand method, which will then pass back to UI tier.
	 * 
	 * @param remainingCommand
	 *            The remaining command that were truncated
	 * @return Success object
	 */
	// @author A0111916M
	private Success executeDisplayCommand(String remainingCommand) {

		Success status;

		if (remainingCommand == null) {
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		} else {

			DisplayParser displayParser = new DisplayParser();

			dataParser.setLastRetrieve(KeywordConstant.KEYWORD_DISPLAY);
			remainingCommand = remainingCommand.trim();

			status = displayParser.parseDisplayCommand(remainingCommand);

			dataParser.setRetrievedTaskList(null);
			if (status.isSuccess() == true) {
				dataParser.appendLastRetrieve(remainingCommand);
				if (status.getObj() != null) {
					dataParser.setRetrievedTaskList((ArrayList<Task>) status
							.getObj());
				}
			}
		}

		return status;
	}

	/**
	 * Begin executing "retrieve" command. It will call RetrieveParser to
	 * continue parsing command for "retrieve". It will pass back a Success
	 * object to parseCommand method, which will then pass back to UI tier.
	 * 
	 * @param remainingCommand
	 *            The remaining command that were truncated
	 * @return Success object
	 */
	// @author A0111837J
	private Success executeRetrieveCommand(String remainingCommand) {

		Success status;

		if (remainingCommand == null) {
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		} else {

			RetrieveParser retrieveParser = new RetrieveParser(keywordFullMap,
					dataParser);

			dataParser.setLastRetrieve(KeywordConstant.KEYWORD_RETRIEVE);
			remainingCommand = remainingCommand.trim();

			status = retrieveParser.parseRetrieveCommand(remainingCommand);

			dataParser.setRetrievedTaskList(null);
			if (status.isSuccess() == true) {
				dataParser.appendLastRetrieve(remainingCommand);
				if (status.getObj() != null) {
					dataParser.setRetrievedTaskList((ArrayList<Task>) status
							.getObj());
				}
			}
		}

		return status;
	}

	/**
	 * Begin executing "delete" command. It will call DeleteParser to continue
	 * parsing command for "delete". It will pass back a Success object to
	 * parseCommand method, which will then pass back to UI tier.
	 * 
	 * @param remainingCommand
	 *            The remaining command that were truncated
	 * @return Success object
	 */
	// @author A0111916M
	private Success executeDeleteCommand(String remainingCommand) {

		Success status;

		if (remainingCommand == null) {
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		} else {

			DeleteParser deleteParser = new DeleteParser(keywordFullMap,
					dataParser);
			remainingCommand = remainingCommand.trim();

			status = deleteParser.parseDeleteCommand(remainingCommand);
		}

		return status;
	}

	/**
	 * Begin executing "update" command. It will call UpdateParser to continue
	 * parsing command for "update". It will pass back a Success object to
	 * parseCommand method, which will then pass back to UI tier.
	 * 
	 * @param remainingCommand
	 *            The remaining command that were truncated
	 * @return Success object
	 */
	// @author A0111916M
	private Success executeUpdateCommand(String remainingCommand) {

		Success status;

		if (remainingCommand == null) {
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		} else {

			UpdateParser updateParser = new UpdateParser(keywordFullMap,
					dataParser);

			remainingCommand = remainingCommand.trim();

			status = updateParser.parseUpdateCommand(remainingCommand);
		}

		return status;
	}

	/**
	 * Begin executing "add" command. It will call AddParser to continue parsing
	 * command for "add". It will pass back a Success object to parseCommand
	 * method, which will then pass back to UI tier.
	 * 
	 * @param remainingCommand
	 *            The remaining command that were truncated
	 * @return Success object
	 */
	// @author A0111916M
	private Success executeAddCommand(String remainingCommand) {

		Success status;

		if (remainingCommand == null) {
			status = new Success(false, Message.FAIL_PARSE_COMMAND);
		} else {

			AddParser addParser = new AddParser(keywordFullMap);
			remainingCommand = remainingCommand.trim();

			status = addParser.processAddCommand(remainingCommand, dataParser);
		}

		return status;
	}
}