package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import logic.Engine;
import resource.Message;
import entity.Success;
import entity.Task;

public class DeleteParser {

	private Engine engine = null;
	private Map<String, String> keywordFullMap = null;
	private DataParser dataParser = null;
	
	public DeleteParser(Map<String, String> keywordFullMap, DataParser dataParser) {
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
	protected Success parseDeleteCommand(String index) {

		Success status = null;
		AuxParser auxParser = new AuxParser(keywordFullMap, dataParser);
		index = index.trim();
		Scanner sc = new Scanner(index);
		List<Task> taskToDeleteList = new ArrayList<>();

		try {

			List<Integer> indexList = new ArrayList<Integer>();

			while (sc.hasNext()) {
				int currIndex = Integer.parseInt(sc.next());
				if (currIndex <= 0 || currIndex > dataParser.getRetrievedTaskList().size()) {
					sc.close();
					throw new IndexOutOfBoundsException(
							Message.ERROR_DELETE_INVALID_INDEX);
				}
				indexList.add(currIndex);
			}

			for (int i = 0; i < indexList.size(); i++) {

				int unfixedIndex = indexList.get(i);
				int indexOffset = unfixedIndex - 1;
				Task taskToRemove = dataParser.getRetrievedTaskList().get(indexOffset);

				taskToDeleteList.add(taskToRemove);
			}
			
			status = engine.deleteTask(taskToDeleteList);
			auxParser.secondaryListRetrieval(status);
			
		} catch (NumberFormatException e) {
			status = new Success(false, Message.ERROR_DELETE_IS_NAN);
		} catch (IndexOutOfBoundsException e) {
			status = new Success(false, Message.ERROR_DELETE_INVALID_INDEX);
		} catch (NullPointerException e) {
			status = new Success(false, Message.ERROR_DELETE_NO_TASK_LIST);
		} catch (Exception e) {
			status = new Success(false, Message.ERROR_DELETE);
		}
		
		sc.close();

		return status;
	}
}
