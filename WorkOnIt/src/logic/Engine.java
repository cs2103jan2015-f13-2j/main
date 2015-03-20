package logic;

// 
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import resource.KeywordConstant;
import data.FileIO;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.Task;
import entity.Success;
import entity.TaskHistory;

public class Engine {

	private final static String SUCCESS_MESSAGE = "List successfully retrived";
	private final static String FAIL_MESSAGE = "List fail to retrived";

	private Stack<TaskHistory> undoStack = new Stack<TaskHistory>();

	// save task into database
	public Success addTask(Task task) {

		Success status = null;

		FileIO dataStorage = new FileIO();
		status = dataStorage.saveIntoFile(task);

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_ADD, task);
		undoStack.push(taskHistoryObj);

		return status;
	}

	// retrieve all task from database
	public Success retrieveTask() {

		Success successObj;

		try {
			List<Task> taskList = new ArrayList<Task>();

			FileIO dataStorage = new FileIO();

			taskList.addAll((ArrayList) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_FLOATING_TASK).getObj());
			taskList.addAll((ArrayList) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_NORMAL_TASK).getObj());
			taskList.addAll((ArrayList) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_DEADLINE_TASK).getObj());
			taskList.addAll((ArrayList) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_RECUR_TASK).getObj());

			successObj = new Success(taskList, true, SUCCESS_MESSAGE);

		} catch (Exception e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;

	}

	// retrieve selected task entity from database.
	public Success retrieveTask(Task task) throws IOException {

		Success succesObj = null;
		String file_type = null;

		FileIO dataStorage = new FileIO();

		if (task instanceof FloatingTask) {
			file_type = KeywordConstant.KEYWORD_FLOATING_TASK;
		}

		if (task instanceof NormalTask) {
			file_type = KeywordConstant.KEYWORD_NORMAL_TASK;
		}

		if (task instanceof RecurrenceTask) {
			file_type = KeywordConstant.KEYWORD_RECUR_TASK;
		}

		if (task instanceof DeadlineTask) {
			file_type = KeywordConstant.KEYWORD_DEADLINE_TASK;
		}

		succesObj = dataStorage.loadFromFileTask(file_type);

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

	public Success retrieveTask(int priority, Date date) throws IOException {

		Success succesObj = null;
		FileIO dataStorage = new FileIO();

		succesObj = dataStorage.loadFromPriorityAndDate(priority, date);

		return succesObj;
	}

	public Success retrieveTask(int priority, Date startDate, Date endDate)
			throws IOException {

		Success succesObj = null;
		FileIO dataStorage = new FileIO();

		succesObj = dataStorage.loadFromPriorityBetweenDate(priority,
				startDate, endDate);

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

	public Success searchTask(String keyword) {
		Success successObj = null;
		FileIO dataStorage = new FileIO();

		successObj = dataStorage.searchFromFile(keyword);

		return successObj;
	}

	public Success searchTask(String keyword, Date date) {
		Success successObj = null;
		FileIO dataStorage = new FileIO();

		successObj = dataStorage.searchFromFileWithDate(keyword, date);

		return successObj;
	}

	public Success searchTask(String keyword, Date startDate, Date endDate) {
		Success successObj = null;
		FileIO dataStorage = new FileIO();

		successObj = dataStorage.searchFromFileBetweenDate(keyword, startDate,
				endDate);

		return successObj;
	}

	// delete task with specific ID
	public Success deleteTask(Task task) {
		Success successObj = null;
		FileIO dataStorage = new FileIO();

		successObj = dataStorage.deleteFromFile(task);

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_DELETE, task);
		undoStack.push(taskHistoryObj);

		return successObj;
	}

	public Success updateTask(Task taskUpdate, Task taskOld) {
		Success successObj = null;
		FileIO dataStorage = new FileIO();

		successObj = dataStorage.updateFromFile(taskUpdate, taskOld);

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_UPDATE, taskUpdate, taskOld);
		undoStack.push(taskHistoryObj);

		return successObj;
	}

	public Success undoTask() {

		Success status = null;
		FileIO dataStorage = new FileIO();

		if (undoStack.size() > 0) {
			TaskHistory undoTask = undoStack.pop();
			String undoOperation = undoTask.getOperation();
			Task taskObj = undoTask.getTask();

			// delete the task obj.
			if (undoOperation.equalsIgnoreCase(KeywordConstant.KEYWORD_ADD)) {
				status = dataStorage.deleteFromFile(taskObj);
			}
			// add the taskObj.
			else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DELETE)) {
				status = dataStorage.saveIntoFile(taskObj);
			}
			// revert the taskObj to previous version.
			else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UPDATE)) {
				Task taskToRevert = undoTask.getAuxTask();
				status = dataStorage.updateFromFile(taskToRevert, taskObj);
			}
		} else {
			status = new Success(false, "Can't undo");
		}
		return status;
	}

}