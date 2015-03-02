package logic;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data.FileIO;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.Task;
import entity.Success;

public class Engine {

	final static String SUCCESS_MESSAGE = "List successfully retrived";
	final static String FAIL_MESSAGE = "List fail to retrived";

	// save task into database
	public Success insertIntoFile(Task task) {

		Success status = null;

		FileIO dataStorage = new FileIO();
		status = dataStorage.saveIntoFile(task);

		return status;
	}

	// retrieve all task from database
	public Success retrieveTask() {

		Success successObj;

		try {
			List<Task> taskList = new ArrayList<Task>();

			FileIO dataStorage = new FileIO();

			taskList.addAll((ArrayList) dataStorage.loadFromFileFloatingTask()
					.getObj());
			taskList.addAll((ArrayList) dataStorage.loadFromFileNormalTask()
					.getObj());
			taskList.addAll((ArrayList) dataStorage.loadFromFileRecurTask()
					.getObj());
			taskList.addAll((ArrayList) dataStorage.loadFromFileDeadlineTask()
					.getObj());

			successObj = new Success(taskList, true, SUCCESS_MESSAGE);

		} catch (Exception e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;

	}

	// retrieve selected task entity from database.
	public Success retrieveTask(Task task) throws IOException {

		Task object = null;
		Success succesObj = null;

		List<Task> taskList = new ArrayList<Task>();
		FileIO dataStorage = new FileIO();

		if (task instanceof FloatingTask) {
			succesObj = dataStorage.loadFromFileFloatingTask();
		}
		;
		if (task instanceof NormalTask) {
			succesObj = dataStorage.loadFromFileNormalTask();
		}
		;
		if (task instanceof RecurrenceTask) {
			succesObj = dataStorage.loadFromFileRecurTask();
		}
		;
		if (task instanceof DeadlineTask) {
			succesObj = dataStorage.loadFromFileDeadlineTask();
		}
		;

		return succesObj;
	}

	// retrieve task with specific START DATE
	// Affected File > NormalTask, DeadlineTask, RecurTask
	public Success retrieveTask(Date date) throws IOException {

		Success succesObj = null;
		// i check the date.
		FileIO dataStorage = new FileIO();

		succesObj = dataStorage.loadFromStartDate(date);

		return succesObj;
	}

	// retrieve task with specific INBETWEEN DATE
	// Affected File > NormalTask, DeadlineTask, RecurTask
	public Success retrieveTask(Date startDate, Date endDate)
			throws IOException {

		Success succesObj = null;

		FileIO dataStorage = new FileIO();

		succesObj = dataStorage.loadFromBetweenDate(startDate, endDate);

		return succesObj;
	}

	// retrieve task with specific priority (EG. URGENT)
	// Affected File > NormalTask, DeadlineTask, RecurTask
	public Success retrieveTask(int priority) throws IOException {

		Success succesObj = null;
		FileIO dataStorage = new FileIO();

		succesObj = dataStorage.loadFromPriority(priority);

		return succesObj;
	}

	// retrieve task with specific task and date (EG. URGENT)
	// Affected File > NormalTask, DeadlineTask, RecurTask
	public Success retrieveTask(Task task, Date date) throws IOException {

		Success succesObj = null;
		FileIO dataStorage = new FileIO();

		succesObj = dataStorage.loadFromStartDateWithTask(task, date);

		return succesObj;
	}

	// retrieve task with specific priority (EG. URGENT)
	// Affected File > NormalTask, DeadlineTask, RecurTask
	public Success retrieveTask(Task task, Date startDate, Date endDate)
			throws IOException {

		Success succesObj = null;
		FileIO dataStorage = new FileIO();

		succesObj = dataStorage.loadFromBetweenDateWithTask(task, startDate,
				endDate);

		return succesObj;
	}

}