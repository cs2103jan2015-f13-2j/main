package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import logic.Engine;
import resource.Message;
import entity.Success;
import entity.Task;

public class DeleteParser {

	private Engine engine = null;
	private Map<String, String> keywordFullMap = null;
	private DataParser dataParser = null;
	private static final Logger LOGGER = Logger.getLogger(DeleteParser.class
			.getName());

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
	//@author A0111916M
	public DeleteParser(Map<String, String> keywordFullMap,
			DataParser dataParser) {
		this.keywordFullMap = keywordFullMap;
		this.dataParser = dataParser;
		engine = new Engine();

		LOGGER.fine("Delete Parser instantiated");
	}

	/**
	 * This will parse the delete command. It will call Engine and will receive
	 * a Success object. This Success object will be returned back to
	 * CommandParser, which will then return to UI tier.
	 * 
	 * @param index
	 *            index of task to be deleted
	 * @return Success object
	 */

	protected Success parseDeleteCommand(String index) {

		assert (index != null);
		assert (keywordFullMap != null);
		assert (!keywordFullMap.isEmpty());

		LOGGER.fine("Parsing delete command");

		Success status = null;
		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
		index = index.trim();
		Scanner sc = new Scanner(index);
		List<Task> taskToDeleteList = new ArrayList<>();

		try {

			List<Integer> indexList = new ArrayList<Integer>();

			while (sc.hasNext()) {
				int currIndex = Integer.parseInt(sc.next());
				if (currIndex <= 0
						|| currIndex > dataParser.getRetrievedTaskList().size()) {
					sc.close();
					throw new IndexOutOfBoundsException(
							Message.ERROR_DELETE_INVALID_INDEX);
				}
				indexList.add(currIndex);
			}

			for (int i = 0; i < indexList.size(); i++) {

				int unfixedIndex = indexList.get(i);
				int indexOffset = unfixedIndex - 1;
				Task taskToRemove = dataParser.getRetrievedTaskList().get(
						indexOffset);

				taskToDeleteList.add(taskToRemove);
			}

			status = engine.deleteTask(taskToDeleteList);
			auxParser.secondaryListRetrieval(status);

		} catch (NumberFormatException e) {
			LOGGER.warning(Message.ERROR_DELETE_IS_NAN);
			status = new Success(false, Message.ERROR_DELETE_IS_NAN);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.warning(Message.ERROR_DELETE_INVALID_INDEX);
			status = new Success(false, Message.ERROR_DELETE_INVALID_INDEX);
		} catch (NullPointerException e) {
			LOGGER.warning(Message.ERROR_DELETE_NO_TASK_LIST);
			status = new Success(false, Message.ERROR_DELETE_NO_TASK_LIST);
		} catch (Exception e) {
			LOGGER.warning(Message.ERROR_DELETE);
			status = new Success(false, Message.ERROR_DELETE);
		}

		sc.close();

		LOGGER.fine("Delete command returns with Success value : "
				+ status.isSuccess());

		return status;
	}
}
