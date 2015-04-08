package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import logic.Validator;
import data.InitFileIO;

import org.junit.Test;

import entity.Success;
import entity.Task;

public class WorkOnItTest {

	Validator testValidator = null;
	InitFileIO initFile = null;
	List<String> addCommandList = null;

	public void initTestEnvironment() {
		initFile = new InitFileIO();
		initFile.checkAndProcessFile();
		testValidator = new Validator();
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
			testValidator.parseCommand(currCommand);

		}
	}

	public void clearTask() {

		addCommandList = new ArrayList<String>();

		String retrieveCommand = "retrieve all";
		String delCommand = "delete ";

		Success status = testValidator.parseCommand(retrieveCommand);
		Object obj = status.getObj();

		if (status.isSuccess()) {

			List<Task> taskList = (ArrayList<Task>) obj;

			for (int i = 1; i <= taskList.size(); i++) {

				delCommand += i;

				if (i != taskList.size()) {
					delCommand += " ";
				}
			}

			testValidator.parseCommand(delCommand);

			addCommandList = new ArrayList<String>();
		}
	}

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

		Collections.sort(taskAddedList);

		// execute search all command
		Success status = testValidator.parseCommand(command);
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
			System.out.println(retrievedList.toString());
			System.out.println(taskAddedList.toString());
			assertEquals(retrievedList, taskAddedList);
		}
	}

	@Test
	public void testRetrieveWrongSpelling() {

		initTestEnvironment();
		addTask();

		String command = "retrievee";

		Success status = testValidator.parseCommand(command);

		assertFalse(status.isSuccess());
	}

	@Test
	public void testRetrieveDescriptionNoResult() {

		initTestEnvironment();
		addTask();

		String command = "cat";

		Success status = testValidator.parseCommand(command);
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

		String command = "search task";

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

		Collections.sort(taskAddedList);

		// execute search all command
		Success status = testValidator.parseCommand(command);
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

			assertEquals(retrievedList, taskAddedList);
		}
	}
	
	@Test
	public void testRetrieveStartEndDate() {

		initTestEnvironment();
		addTask();

		String command = "search from 9 April to 10 April";

		List<String> taskAddedList = new ArrayList<String>();

		// processing expected output
		taskAddedList
				.add("task three from 08 Apr 2015,  5:31:00 PM to 09 Apr 2015,  5:31:00 PM");
		taskAddedList
				.add("task four from 09 Apr 2015,  12:00:01 AM to 10 Apr 2015,  10:04:00 AM");
		taskAddedList
				.add("task nine by 10 Apr 2015,  8:21:00 PM");
		taskAddedList.add("task eight by 10 Apr 2015,  11:59:59 PM");

		Collections.sort(taskAddedList);

		// execute search all command
		Success status = testValidator.parseCommand(command);
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

			assertEquals(retrievedList, taskAddedList);
		}
	}
}
