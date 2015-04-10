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

	public UpdateParser(Map<String, String> keywordFullMap, DataParser dataParser) {
		this.keywordFullMap = keywordFullMap;
		this.dataParser = dataParser;
		engine = new Engine();
	}

	/**
	 *
	 * @param
	 * @return
	 */
	// @author
	protected Success parseUpdateCommand(String remainingCommand) {

		Success status = null;
		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
		AddParser addParser = new AddParser(keywordFullMap, dataParser);
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
				System.out.println("tasktoremove null");
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
