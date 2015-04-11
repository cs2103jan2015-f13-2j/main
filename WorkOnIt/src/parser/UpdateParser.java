package parser;

import java.util.ArrayList;
import java.util.Map;

import logic.Engine;
import resource.KeywordConstant;
import resource.Message;
import entity.Success;
import entity.Task;

public class UpdateParser {

	private Map<String, String> keywordFullMap = null;
	private Engine engine = null;
	private DataParser dataParser = null;

	/**
	 * This constructor takes in a full map of keywords, if any. It will make
	 * use of the data in data parser to manipulate information.
	 * 
	 * @param keywordFullMap
	 *            a hash map of first string mapped onto second string
	 * @param dataParser
	 *            the data that contains current information
	 * @return
	 */
	// @author A0111916M
	public UpdateParser(Map<String, String> keywordFullMap,
			DataParser dataParser) {
		this.keywordFullMap = keywordFullMap;
		this.dataParser = dataParser;
		engine = new Engine();
	}

	/**
	 * This method will parse the update command. The first time this method is
	 * being called, it will take the appropriate Task in retrievedTaskList(if
	 * any), and display it to the user. When the second time this method is
	 * being called, it will take in the new updated Task (in String form) and
	 * parse it into a Task object. It will then gives to Engine to perform the
	 * necessary updates.
	 * 
	 * @param remainingCommand
	 * @return
	 */
	// @author A0111916M
	protected Success parseUpdateCommand(String remainingCommand) {

		assert(remainingCommand != null);
		
		Success status = null;
		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
		AddParser addParser = new AddParser(keywordFullMap);
		ArrayList<Task> retrievedTaskList = dataParser.getRetrievedTaskList();
		remainingCommand = remainingCommand.trim();

		try {
			int indexOffset = Integer.parseInt(remainingCommand) - 1;
			Task taskToRemove = retrievedTaskList.get(indexOffset);
			dataParser.setTaskToRemove(taskToRemove);
			String taskDisplay = KeywordConstant.KEYWORD_UPDATE;
			taskDisplay += " " + taskToRemove.toDisplay();

			status = new Success(taskDisplay, true, null);

		} catch (NumberFormatException e) {

			Task taskToRemove = dataParser.getTaskToRemove();

			if (taskToRemove != null) {

				Success statusTask = addParser
						.parseAddCommand(remainingCommand);

				if (statusTask.isSuccess()) {
					Task updatedTask = (Task) statusTask.getObj();
					status = engine.updateTask(updatedTask, taskToRemove);

					if (status.isSuccess()) {
						auxParser.secondaryListRetrieval(status);
						dataParser.setTaskToRemove(null);
					}
				}
			} else {

				status = new Success(false, Message.ERROR_UPDATE_NO_TASK_LIST);
			}
		} catch (IndexOutOfBoundsException e) {
			status = new Success(false, Message.ERROR_UPDATE_INVALID_INDEX);
		} catch (NullPointerException e) {
			status = new Success(false, Message.ERROR_UPDATE_NO_TASK_LIST);
		} catch (Exception e) {
			status = new Success(false, Message.ERROR_UPDATE);
		}

		return status;
	}
}
