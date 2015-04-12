package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import logic.Engine;
import resource.Message;
import entity.Success;
import entity.Task;

public class DoneParser {

	private Engine engine = null;
	private Map<String, String> keywordFullMap = null;
	private DataParser dataParser = null;

	/**
	 * This constructor takes in a full map of keywords, if any. It will make
	 * use of the data in data parser to manipulate information.
	 * 
	 * @param keywordFullMap
	 *            a hash map of first string mapped onto second string
	 * @param dataParser
	 *            the data that contains current information
	 */
	// @author A0111916M
	public DoneParser(Map<String, String> keywordFullMap, DataParser dataParser) {
		this.keywordFullMap = keywordFullMap;
		this.dataParser = dataParser;
		this.engine = new Engine();
	}

	/**
	 * This method execute the done command. The input parameter
	 * remainingCommand will contains the numbering of Task(s) that needed to be
	 * done. It will form up the Task object(s) into a List, and pass it to
	 * Engine to perform the necessary logic.
	 * 
	 * @param remainingCommand
	 *            the remaining command after being truncated
	 * @return Success object
	 */
	// @author A0111916M
	protected Success doneCommand(String remainingCommand) {

		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		Success status = null;

		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
		ArrayList<Task> retrievedTaskList = dataParser.getRetrievedTaskList();

		remainingCommand = remainingCommand.trim();
		Scanner sc = new Scanner(remainingCommand);
		List<Task> doneList = new ArrayList<Task>();

		try {
			while (sc.hasNext()) {

				String currentValue = sc.next();
				int indexOffset = Integer.parseInt(currentValue) - 1;
				Task doneTask = retrievedTaskList.get(indexOffset);
				doneList.add(doneTask);
			}

			status = engine.markAsDone(doneList);
			auxParser.secondaryListRetrieval(status);
		} catch (NumberFormatException e) {
			status = new Success(false, Message.ERROR_DONE_IS_NAN);
		} catch (IndexOutOfBoundsException e) {
			status = new Success(false, Message.ERROR_DONE_INVALID_INDEX);
		} catch (NullPointerException e) {
			status = new Success(false, Message.ERROR_DONE_NO_TASK_LIST);
		} catch (Exception e) {
			status = new Success(false, Message.ERROR_DONE);
		}

		sc.close();

		return status;
	}

	/**
	 * This method execute the undone command. The input parameter
	 * remainingCommand will contains the numbering of Task(s) that needed to be
	 * undone. It will form up the Task object(s) into a List, and pass it to
	 * Engine to perform the necessary logic.
	 * 
	 * @param remainingCommand
	 *            the remaining command after being truncated
	 * @return Success object
	 */
	// @author A0111916M
	protected Success undoneCommand(String remainingCommand) {

		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		Success status = null;

		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
		ArrayList<Task> retrievedTaskList = dataParser.getRetrievedTaskList();

		remainingCommand = remainingCommand.trim();
		Scanner sc = new Scanner(remainingCommand);
		List<Task> undoneList = new ArrayList<Task>();

		try {
			while (sc.hasNext()) {

				String currentValue = sc.next();
				int indexOffset = Integer.parseInt(currentValue) - 1;
				Task undoneTask = retrievedTaskList.get(indexOffset);
				undoneList.add(undoneTask);
			}

			status = engine.markAsUndone(undoneList);
			auxParser.secondaryListRetrieval(status);
		} catch (NumberFormatException e) {
			status = new Success(false, Message.ERROR_UNDONE_IS_NAN);
		} catch (IndexOutOfBoundsException e) {
			status = new Success(false, Message.ERROR_UNDONE_INVALID_INDEX);
		} catch (NullPointerException e) {
			status = new Success(false, Message.ERROR_UNDONE_NO_TASK_LIST);
		} catch (Exception e) {
			status = new Success(false, Message.ERROR_UNDONE);
		}

		sc.close();

		return status;
	}
}
