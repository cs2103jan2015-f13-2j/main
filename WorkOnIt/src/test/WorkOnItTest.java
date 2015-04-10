package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.InitFileIO;

import org.junit.Test;

import parser.CommandParser;
import validator.Validator;
import entity.Success;
import entity.Task;

public class WorkOnItTest {

	CommandParser commandParser = null;
	Validator keywordValidator = null;
	InitFileIO initFile = null;
	List<String> addCommandList = null;

	public void initTestEnvironment() {
		initFile = new InitFileIO();
		initFile.checkAndProcessFile();
		commandParser = new CommandParser();
		keywordValidator = new Validator();
		addCommandList = new ArrayList<String>();
	}

	public void addTask() {

		clearTask();

		// add task with a single date
		String testCase1 = "add task one on 8 Apr";
		addCommandList.add(testCase1);

		// add task with a start date of 0000H to end date of 2359H
		String testCase2 = "add task two from 7 April to 8 April";
		addCommandList.add(testCase2);

		// add task with a start date >0000H to end date of 2359H
		String testCase3 = "add task three from 8 April 5:31pm to 9 April";
		addCommandList.add(testCase3);

		// add task with a start date 0000H to end date of <2359H
		String testCase4 = "add task four from 9 April to 10 April 10:04am";
		addCommandList.add(testCase4);

		// add low priority task
		String testCase5 = "add task five on 8 April 3pm priority low";
		addCommandList.add(testCase5);

		// add medium priority task
		String testCase6 = "add task six on 8 April 3pm priority medium";
		addCommandList.add(testCase6);

		// add high priority task
		String testCase7 = "add task seven on 8 April 3pm priority high";
		addCommandList.add(testCase7);

		// add deadline task 2359H
		String testCase8 = "add task eight by 10 Apr";
		addCommandList.add(testCase8);

		// add deadline task <2359H
		String testCase9 = "add task nine by 10 Apr 8:21pm";
		addCommandList.add(testCase9);

		// add floating task
		String testCase10 = "add task ten";
		addCommandList.add(testCase10);

		for (int i = 0; i < addCommandList.size(); i++) {
			String currCommand = addCommandList.get(i);
			commandParser.parseCommand(currCommand);
		}
	}

	public void clearTask() {

		addCommandList = new ArrayList<String>();

		String retrieveCommand = "retrieve all";
		String delCommand = "delete ";

		Success status = commandParser.parseCommand(retrieveCommand);
		Object obj = status.getObj();

		if (status.isSuccess()) {

			List<Task> taskList = (ArrayList<Task>) obj;

			for (int i = 1; i <= taskList.size(); i++) {

				delCommand += i;

				if (i != taskList.size()) {
					delCommand += " ";
				}
			}

			commandParser.parseCommand(delCommand);

			addCommandList = new ArrayList<String>();
		}
	}

	private void testEquals(String command, List<String> taskAddedList) {

		Collections.sort(taskAddedList);

		// execute retrieve all command
		Success status = commandParser.parseCommand(command);
		Object obj = status.getObj();

		if (status.isSuccess() && obj instanceof ArrayList<?>) {

			// process retrieved tasks
			List<Task> retrievedTaskList = (ArrayList<Task>) obj;

			List<String> retrievedList = new ArrayList<String>();

			for (int i = 0; i < retrievedTaskList.size(); i++) {

				Task currTask = retrievedTaskList.get(i);
				retrievedList.add(currTask.toDisplay());
			}

			Collections.sort(retrievedList);

			assertEquals(taskAddedList, retrievedList);
		}
	}

	/***********************************
	 * TEST CASE FOR KEYWORD VALIDATOR
	 ***********************************/

	@Test
	public void testKeyword() {

		initTestEnvironment();
		addTask();

		String keyword = "add";

		boolean isKeyword = keywordValidator.validateKeyword(keyword);

		assertTrue(isKeyword);
	}

	@Test
	public void testInvalidKeyword() {

		initTestEnvironment();
		addTask();

		String command = "retrievee";

		boolean isKeyword = keywordValidator.validateKeyword(command);

		assertFalse(isKeyword);
	}

	@Test
	public void testKeywordSequence() {

		initTestEnvironment();
		addTask();

		List<String> keywordList = new ArrayList<String>();

		keywordList.add("add");
		keywordList.add("on");
		keywordList.add("from");
		keywordList.add("to");
		keywordList.add("priority");

		boolean isKeyword = keywordValidator
				.validateKeywordSequence(keywordList);

		assertTrue(isKeyword);
	}

	@Test
	public void testInvalidKeywordSequence() {

		initTestEnvironment();
		addTask();

		List<String> keywordList = new ArrayList<String>();

		keywordList.add("add");
		keywordList.add("on");
		keywordList.add("to");
		keywordList.add("from");
		keywordList.add("priority");

		boolean isKeyword = keywordValidator
				.validateKeywordSequence(keywordList);

		assertFalse(isKeyword);
	}

	@Test
	public void testKeywordMapping() {

		initTestEnvironment();
		addTask();

		List<String> keywordList = new ArrayList<String>();

		keywordList.add("create");
		keywordList.add("on");
		keywordList.add("from");
		keywordList.add("until");
		keywordList.add("priority");
		keywordList.add("urgent");

		boolean isKeyword = keywordValidator
				.validateKeywordSequence(keywordList);

		assertTrue(isKeyword);
	}

	@Test
	public void testInvalidKeywordMapping() {

		initTestEnvironment();
		addTask();

		List<String> keywordList = new ArrayList<String>();

		keywordList.add("create");
		keywordList.add("on");
		keywordList.add("since");
		keywordList.add("until");
		keywordList.add("priority");
		keywordList.add("urgent");

		boolean isKeyword = keywordValidator
				.validateKeywordSequence(keywordList);

		assertFalse(isKeyword);
	}

	/***********************************
	 * TEST CASE FOR ADD COMMAND
	 ***********************************/

	@Test
	public void testAddTasks() {

		initTestEnvironment();
		addTask();

		String command = "retrieve all";
		List<String> taskAddedList = new ArrayList<String>();

		// processing expected output
		taskAddedList
				.add("task four from 09 Apr 2015,  12:00:01 AM to 10 Apr 2015,  10:04:00 AM");
		taskAddedList
				.add("task two from 07 Apr 2015,  12:00:01 AM to 08 Apr 2015,  11:59:59 PM");
		taskAddedList
				.add("task three from 08 Apr 2015,  5:31:00 PM to 09 Apr 2015,  5:31:00 PM");
		taskAddedList.add("task one on 08 Apr 2015,  12:00:01 AM");
		taskAddedList.add("task seven on 08 Apr 2015,  3:00:00 PM priority 2");
		taskAddedList.add("task six on 08 Apr 2015,  3:00:00 PM");
		taskAddedList.add("task five on 08 Apr 2015,  3:00:00 PM priority 0");
		taskAddedList.add("task nine by 10 Apr 2015,  8:21:00 PM");
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");
		taskAddedList.add("task ten");

		testEquals(command, taskAddedList);
	}

	@Test
	public void testAddInvalidStartDateNormal() {

		initTestEnvironment();
		addTask();

		String currCommand = "add invalid start date from nowhere to 8 April";

		Success status = commandParser.parseCommand(currCommand);

		assertFalse(!status.isSuccess());
	}

	@Test
	public void testAddInvalidEndDateNormal() {

		initTestEnvironment();
		addTask();

		String currCommand = "add invalid end date from 8 April to nowhere";

		Success status = commandParser.parseCommand(currCommand);

		assertFalse(!status.isSuccess());
	}

	@Test
	public void testAddInvalidDeadlineDate() {

		initTestEnvironment();
		addTask();

		String currCommand = "add invalid deadline date by nowhere";

		Success status = commandParser.parseCommand(currCommand);

		assertFalse(!status.isSuccess());
	}

	// @Test
	// public void testAddInvalidNegativePriority() {
	//
	// initTestEnvironment();
	// addTask();
	//
	// String currCommand = "add invalid negative priority -1";
	//
	// Success status = testValidator.parseCommand(currCommand);
	//
	// assertFalse(!status.isSuccess());
	// }
	//
	// @Test
	// public void testAddInvalidPositivePriority() {
	//
	// initTestEnvironment();
	// addTask();
	//
	// String currCommand = "add invalid negative priority 3";
	//
	// Success status = testValidator.parseCommand(currCommand);
	//
	// assertFalse(!status.isSuccess());
	// }

	/***********************************
	 * TEST CASE FOR RETRIEVE COMMAND
	 ***********************************/

	@Test
	public void testRetrieveDescriptionNoResult() {

		initTestEnvironment();
		addTask();

		String command = "retrieve cat";

		Success status = commandParser.parseCommand(command);
		Object obj = status.getObj();

		if ((status.isSuccess() && obj instanceof ArrayList<?>)) {

			List<Task> retrievedTaskList = (ArrayList<Task>) obj;

			assertEquals(0, retrievedTaskList.size());
		}
	}

	@Test
	public void testRetrieveDescriptionWithResult() {

		initTestEnvironment();
		addTask();

		String command = "retrieve task";

		List<String> taskAddedList = new ArrayList<String>();

		// processing expected output
		taskAddedList
				.add("task four from 09 Apr 2015,  12:00:01 AM to 10 Apr 2015,  10:04:00 AM");
		taskAddedList
				.add("task two from 07 Apr 2015,  12:00:01 AM to 08 Apr 2015,  11:59:59 PM");
		taskAddedList
				.add("task three from 08 Apr 2015,  5:31:00 PM to 09 Apr 2015,  5:31:00 PM");
		taskAddedList.add("task one on 08 Apr 2015,  12:00:01 AM");
		taskAddedList.add("task seven on 08 Apr 2015,  3:00:00 PM priority 2");
		taskAddedList.add("task six on 08 Apr 2015,  3:00:00 PM");
		taskAddedList.add("task five on 08 Apr 2015,  3:00:00 PM priority 0");
		taskAddedList.add("task nine by 10 Apr 2015,  8:21:00 PM");
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");
		taskAddedList.add("task ten");

		testEquals(command, taskAddedList);
	}

	@Test
	public void testRetrieveStartEndDate() {

		initTestEnvironment();
		addTask();

		String command = "retrieve from 9 April to 10 April";

		List<String> taskAddedList = new ArrayList<String>();

		// processing expected output
		taskAddedList
				.add("task three from 08 Apr 2015,  5:31:00 PM to 09 Apr 2015,  5:31:00 PM");
		taskAddedList
				.add("task four from 09 Apr 2015,  12:00:01 AM to 10 Apr 2015,  10:04:00 AM");
		taskAddedList.add("task nine by 10 Apr 2015,  8:21:00 PM");
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");

		testEquals(command, taskAddedList);
	}

	@Test
	public void testRetrieveOneDate() {

		initTestEnvironment();
		addTask();

		String command = "retrieve on 10 April";

		List<String> taskAddedList = new ArrayList<String>();

		// processing expected output
		taskAddedList
				.add("task four from 09 Apr 2015,  12:00:01 AM to 10 Apr 2015,  10:04:00 AM");
		taskAddedList.add("task nine by 10 Apr 2015,  8:21:00 PM");
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");

		testEquals(command, taskAddedList);
	}

	@Test
	public void testRetrieveOneDateNoResult() {

		initTestEnvironment();
		addTask();

		String command = "retrieve on 20 May";

		Success status = commandParser.parseCommand(command);
		Object obj = status.getObj();

		if ((status.isSuccess() && obj instanceof ArrayList<?>)) {

			List<Task> retrievedTaskList = (ArrayList<Task>) obj;

			assertEquals(0, retrievedTaskList.size());
		}
	}

	@Test
	public void testRetrievePriorityHigh() {

		initTestEnvironment();
		addTask();

		String command = "retrieve priority 2";

		List<String> taskAddedList = new ArrayList<String>();

		// adding expected output
		taskAddedList.add("task seven on 08 Apr 2015,  3:00:00 PM priority 2");

		testEquals(command, taskAddedList);
	}

	@Test
	public void testRetrievePriorityMedium() {

		initTestEnvironment();
		addTask();

		String command = "retrieve priority 1";

		List<String> taskAddedList = new ArrayList<String>();

		// adding expected output
		taskAddedList
				.add("task two from 07 Apr 2015,  12:00:01 AM to 08 Apr 2015,  11:59:59 PM");
		taskAddedList.add("task one on 08 Apr 2015,  12:00:01 AM");
		taskAddedList.add("task six on 08 Apr 2015,  3:00:00 PM");
		taskAddedList
				.add("task three from 08 Apr 2015,  5:31:00 PM to 09 Apr 2015,  5:31:00 PM");
		taskAddedList
				.add("task four from 09 Apr 2015,  12:00:01 AM to 10 Apr 2015,  10:04:00 AM");
		taskAddedList.add("task nine by 10 Apr 2015,  8:21:00 PM");
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");
		taskAddedList.add("task ten");

		testEquals(command, taskAddedList);
	}

	@Test
	public void testRetrievePriorityLow() {

		initTestEnvironment();
		addTask();

		String command = "retrieve priority 0";

		List<String> taskAddedList = new ArrayList<String>();

		// adding expected output
		taskAddedList.add("task five on 08 Apr 2015,  3:00:00 PM priority 0");

		testEquals(command, taskAddedList);
	}

	// @Test
	// public void testRetrieveInvalidNegativePriority() {
	//
	// initTestEnvironment();
	// addTask();
	//
	// String command = "retrieve priority -1";
	//
	// Success status = testValidator.parseCommand(command);
	//
	// assertFalse(status.isSuccess());
	// }
	//
	// @Test
	// public void testRetrieveInvalidPositivePriority() {
	//
	// initTestEnvironment();
	// addTask();
	//
	// String command = "retrieve priority 3";
	//
	// Success status = testValidator.parseCommand(command);
	//
	// assertFalse(status.isSuccess());
	// }

	@Test
	public void testRetrievePrioritySingleDate() {

		initTestEnvironment();
		addTask();

		String command = "retrieve priority 0 at 8 April";

		List<String> taskAddedList = new ArrayList<String>();

		// adding expected output
		taskAddedList.add("task five on 08 Apr 2015,  3:00:00 PM priority 0");

		testEquals(command, taskAddedList);
	}

	@Test
	public void testRetrievePriorityWithDateRange() {

		initTestEnvironment();
		addTask();

		String command = "retrieve priority 1 from 7 April to 8 April";

		List<String> taskAddedList = new ArrayList<String>();

		// adding expected output
		taskAddedList
				.add("task two from 07 Apr 2015,  12:00:01 AM to 08 Apr 2015,  11:59:59 PM");
		taskAddedList.add("task one on 08 Apr 2015,  12:00:01 AM");
		taskAddedList.add("task six on 08 Apr 2015,  3:00:00 PM");
		taskAddedList
				.add("task three from 08 Apr 2015,  5:31:00 PM to 09 Apr 2015,  5:31:00 PM");
		taskAddedList.add("task nine by 10 Apr 2015,  8:21:00 PM");
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");

		testEquals(command, taskAddedList);
	}

	/***********************************
	 * TEST CASE FOR DELETE COMMAND
	 ***********************************/

	@Test
	public void testDeleteMultiple() {

		initTestEnvironment();
		addTask();

		String retrieveCommand = "retrieve 10 April";

		Success status = commandParser.parseCommand(retrieveCommand);

		if (status.isSuccess()) {

			String deleteCommand = "delete 3 1";

			status = commandParser.parseCommand(deleteCommand);
		}

		List<String> taskAddedList = new ArrayList<String>();

		// adding expected output
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");
		testEquals(retrieveCommand, taskAddedList);
	}

	@Test
	public void testDeleteMultipleInvalidLowerBound() {

		initTestEnvironment();
		addTask();

		String retrieveCommand = "retrieve 10 April";

		Success status = commandParser.parseCommand(retrieveCommand);

		if (status.isSuccess()) {

			String deleteCommand = "delete 3 0 1";

			status = commandParser.parseCommand(deleteCommand);
		}

		assertFalse(status.isSuccess());
	}

	@Test
	public void testDeleteMultipleInvalidUpperBound() {

		initTestEnvironment();
		addTask();

		String retrieveCommand = "retrieve 10 April";

		Success status = commandParser.parseCommand(retrieveCommand);

		if (status.isSuccess()) {

			String deleteCommand = "delete 3 1 4";

			status = commandParser.parseCommand(deleteCommand);
		}

		assertFalse(status.isSuccess());
	}

	@Test
	public void testDeleteNaN() {

		initTestEnvironment();
		addTask();

		String retrieveCommand = "retrieve 10 April";

		Success status = commandParser.parseCommand(retrieveCommand);

		if (status.isSuccess()) {

			String deleteCommand = "delete 3 none-numeric 1";

			status = commandParser.parseCommand(deleteCommand);
		}

		assertFalse(status.isSuccess());
	}

	@Test
	public void testDeleteWithoutRetrieve() {

		initTestEnvironment();
		addTask();

		commandParser = new CommandParser();

		String deleteCommand = "delete 3 1";

		Success status = commandParser.parseCommand(deleteCommand);

		assertFalse(status.isSuccess());
	}

	/***********************************
	 * TEST CASE FOR UPDATE COMMAND
	 ***********************************/

	@Test
	public void testUpdate() {

		initTestEnvironment();
		addTask();

		String retrieveCommand = "retrieve 10 April";

		Success status = commandParser.parseCommand(retrieveCommand);
		if (status.isSuccess()) {

			String updateCommand = "update 1";

			status = commandParser.parseCommand(updateCommand);
		}

		if (status.isSuccess()) {

			String updateCommand = "update updated task four from 09 Apr 2015,  12:00:01 AM to 10 Apr 2015,  10:04:00 AM";
			status = commandParser.parseCommand(updateCommand);
		}

		List<String> taskAddedList = new ArrayList<String>();

		// adding expected output
		taskAddedList
				.add("updated task four from 09 Apr 2015,  12:00:01 AM to 10 Apr 2015,  10:04:00 AM");
		taskAddedList.add("task nine by 10 Apr 2015,  8:21:00 PM");
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");

		testEquals(retrieveCommand, taskAddedList);
	}

	@Test
	public void testUpdateInvalidWithoutRetrieve() {

		initTestEnvironment();
		addTask();

		commandParser = new CommandParser();

		String updateCommand = "update 1";

		Success status = commandParser.parseCommand(updateCommand);

		assertFalse(status.isSuccess());
	}

	@Test
	public void testUpdateInvalidLowerBound() {

		initTestEnvironment();
		addTask();

		String retrieveCommand = "retrieve 10 April";

		Success status = commandParser.parseCommand(retrieveCommand);

		if (status.isSuccess()) {

			String updateCommand = "update 0";

			status = commandParser.parseCommand(updateCommand);
		}

		assertFalse(status.isSuccess());
	}

	@Test
	public void testUpdateInvalidUpperBound() {

		initTestEnvironment();
		addTask();

		String retrieveCommand = "retrieve 10 April";

		Success status = commandParser.parseCommand(retrieveCommand);

		if (status.isSuccess()) {

			String updateCommand = "update 4";

			status = commandParser.parseCommand(updateCommand);
		}

		assertFalse(status.isSuccess());
	}

	/***********************************
	 * TEST CASE FOR UNDO COMMAND
	 ***********************************/

	@Test
	public void testUndoMultiple() {

		initTestEnvironment();
		addTask();

		String retrieveCommand = "retrieve 10 April";

		Success status = commandParser.parseCommand(retrieveCommand);

		if (status.isSuccess()) {

			String deleteCommand = "delete 3 1";

			status = commandParser.parseCommand(deleteCommand);
		}

		if (status.isSuccess()) {

			String undoCommand = "undo";

			status = commandParser.parseCommand(undoCommand);
		}

		List<String> taskAddedList = new ArrayList<String>();

		// adding expected output
		taskAddedList
				.add("task four from 09 Apr 2015,  12:00:01 AM to 10 Apr 2015,  10:04:00 AM");
		taskAddedList.add("task nine by 10 Apr 2015,  8:21:00 PM");
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");

		testEquals(retrieveCommand, taskAddedList);
	}

	@Test
	public void testUndoSingle() {

		initTestEnvironment();
		addTask();

		String retrieveCommand = "retrieve 10 April";

		Success status = commandParser.parseCommand(retrieveCommand);

		if (status.isSuccess()) {

			String addCommand = "add mock task on 10 April 5pm";

			status = commandParser.parseCommand(addCommand);
		}

		if (status.isSuccess()) {

			String undoCommand = "undo";

			status = commandParser.parseCommand(undoCommand);
		}

		List<String> taskAddedList = new ArrayList<String>();

		// adding expected output
		taskAddedList
				.add("task four from 09 Apr 2015,  12:00:01 AM to 10 Apr 2015,  10:04:00 AM");
		taskAddedList.add("task nine by 10 Apr 2015,  8:21:00 PM");
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");

		testEquals(retrieveCommand, taskAddedList);
	}

	@Test
	public void testUndoInvalidEmptyStack() {

		initTestEnvironment();
		addTask();

		commandParser = new CommandParser();

		String undoCommand = "undo";

		Success status = commandParser.parseCommand(undoCommand);

		assertFalse(status.isSuccess());
	}

	/***********************************
	 * TEST CASE FOR REDO COMMAND
	 ***********************************/

	@Test
	public void testRedoMultiple() {

		initTestEnvironment();
		addTask();

		String retrieveCommand = "retrieve 10 April";

		Success status = commandParser.parseCommand(retrieveCommand);

		if (status.isSuccess()) {

			String deleteCommand = "delete 3 1";

			status = commandParser.parseCommand(deleteCommand);
		}

		if (status.isSuccess()) {

			String undoCommand = "undo";

			status = commandParser.parseCommand(undoCommand);
		}

		if (status.isSuccess()) {

			String redoCommand = "redo";

			status = commandParser.parseCommand(redoCommand);
		}

		List<String> taskAddedList = new ArrayList<String>();

		// adding expected output
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");

		testEquals(retrieveCommand, taskAddedList);
	}

	@Test
	public void testRedoSingle() {

		initTestEnvironment();
		addTask();

		String retrieveCommand = "retrieve 10 April";

		Success status = commandParser.parseCommand(retrieveCommand);

		if (status.isSuccess()) {

			String addCommand = "add mock task on 10 April 5pm";

			status = commandParser.parseCommand(addCommand);
		}

		if (status.isSuccess()) {

			String undoCommand = "undo";

			status = commandParser.parseCommand(undoCommand);
		}

		if (status.isSuccess()) {

			String redoCommand = "redo";

			status = commandParser.parseCommand(redoCommand);
		}

		List<String> taskAddedList = new ArrayList<String>();

		// adding expected output
		taskAddedList
				.add("task four from 09 Apr 2015,  12:00:01 AM to 10 Apr 2015,  10:04:00 AM");
		taskAddedList.add("task nine by 10 Apr 2015,  8:21:00 PM");
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");
		taskAddedList.add("mock task on 10 Apr 2015,  5:00:00 PM");

		testEquals(retrieveCommand, taskAddedList);
	}

	@Test
	public void testRedoInvalidEmptyStack() {

		initTestEnvironment();
		addTask();

		commandParser = new CommandParser();

		String redoCommand = "redo";

		Success status = commandParser.parseCommand(redoCommand);

		assertFalse(status.isSuccess());
	}
}