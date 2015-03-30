package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import resource.KeywordConstant;
import resource.Message;
import data.FileIO;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
import entity.SuccessDisplay;
import entity.Task;
import entity.Success;
import entity.TaskHistory;

public class Engine {

	private Stack<TaskHistory> undoStack = new Stack<TaskHistory>();
	private Stack<TaskHistory> redoStack = new Stack<TaskHistory>();

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
	public Success retrieveTask(String keyword) {

		Success successObj;

		try {
			List<Task> taskList = new ArrayList<Task>();

			FileIO dataStorage = new FileIO();

			taskList.addAll((ArrayList) dataStorage.loadFromFileTask(keyword)
					.getObj());

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);

		} catch (Exception e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;

	}

	// retrieve task based on keyword
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

			successObj = new Success(taskList, true,
					Message.SUCCESS_RETRIEVE_LIST);

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

	public SuccessDisplay retrieveDisplay(Date startDate, Date endDate,
			String displayType) throws IOException {

		SuccessDisplay successDispObj = null;

		Success successObj = retrieveTask(startDate, endDate);

		if (successObj.isSuccess()) {

			List<Task> taskList = (ArrayList<Task>) successObj.getObj();
			
			System.out.println(startDate);
			System.out.println(endDate);
			
			for(int i = 0; i < taskList.size(); i++) {
				String name = taskList.get(i).getTaskName();
				System.out.println(name);
			}

			successObj = retrieveTask(KeywordConstant.KEYWORD_FLOATING_TASK);

			if (successObj.isSuccess()) {
				List<Task> floatingTaskList = (ArrayList<Task>) successObj
						.getObj();
				taskList.addAll(floatingTaskList);

				successDispObj = new SuccessDisplay(displayType, taskList,
						true, Message.SUCCESS_RETRIEVE_LIST);
			} else {
				successDispObj = new SuccessDisplay(false,
						Message.ERROR_RETRIEVE);
			}

		} else {
			successDispObj = new SuccessDisplay(false, Message.ERROR_RETRIEVE);
		}

		return successDispObj;
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

	public Success markAsDone(List<Task> doneList) {

		Success status = null;
		FileIO dataStorage = new FileIO();
		boolean isMarkAllDone = true;

		for (int i = 0; i < doneList.size(); i++) {

			Task currentTask = doneList.get(i);

			Success successObj = dataStorage.deleteFromFile(currentTask);

			if (!successObj.isSuccess()) {
				isMarkAllDone = false;
			}

			currentTask.setCompleted(true);
			successObj = dataStorage.saveIntoFile(currentTask);

			if (!successObj.isSuccess()) {
				isMarkAllDone = false;
			}
		}

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_DONE, doneList);
		undoStack.push(taskHistoryObj);

		if (isMarkAllDone) {
			status = new Success(true, Message.SUCCESS_MARK_DONE);
		} else {
			status = new Success(false, Message.FAIL_MARK_DONE);
		}

		return status;
	}

	public Success markAsUndone(List<Task> undoneList) {

		Success status = null;
		FileIO dataStorage = new FileIO();
		boolean isMarkAllUndone = true;

		for (int i = 0; i < undoneList.size(); i++) {

			Task currentTask = undoneList.get(i);

			Success successObj = dataStorage.deleteFromFile(currentTask);

			if (!successObj.isSuccess()) {
				isMarkAllUndone = false;
			}

			currentTask.setCompleted(false);
			successObj = dataStorage.saveIntoFile(currentTask);

			if (!successObj.isSuccess()) {
				isMarkAllUndone = false;
			}
		}

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_UNDONE, undoneList);
		undoStack.push(taskHistoryObj);

		if (isMarkAllUndone) {
			status = new Success(true, Message.SUCCESS_MARK_UNDONE);
		} else {
			status = new Success(false, Message.FAIL_MARK_UNDONE);
		}

		return status;
	}

	public Success undoTask() {

		Success status = null;
		FileIO dataStorage = new FileIO();

		if (undoStack.size() > 0) {
			TaskHistory undoTask = undoStack.pop();
			String undoOperation = undoTask.getOperation();

			// delete the task obj.
			if (undoOperation.equalsIgnoreCase(KeywordConstant.KEYWORD_ADD)) {
				Task taskObj = undoTask.getTask();
				status = dataStorage.deleteFromFile(taskObj);
			}
			// add the taskObj.
			else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DELETE)) {
				Task taskObj = undoTask.getTask();
				status = dataStorage.saveIntoFile(taskObj);
			}
			// revert the taskObj to previous version.
			else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UPDATE)) {
				Task taskObj = undoTask.getTask();
				Task taskToRevert = undoTask.getAuxTask();
				status = dataStorage.updateFromFile(taskToRevert, taskObj);

			} else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DONE)) {

				boolean isMarkAllUndone = true;
				List<Task> taskListToRevert = undoTask.getTaskList();

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);

					Success successObj = dataStorage
							.deleteFromFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllUndone = false;
					}

					currentTask.setCompleted(false);
					successObj = dataStorage.saveIntoFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllUndone = false;
					}
				}

				if (isMarkAllUndone) {
					status = new Success(true, Message.SUCCESS_MARK_UNDONE);
				} else {
					status = new Success(false, Message.FAIL_MARK_UNDONE);
				}

			} else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UNDONE)) {

				boolean isMarkAllDone = true;
				List<Task> taskListToRevert = undoTask.getTaskList();

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);

					Success successObj = dataStorage
							.deleteFromFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllDone = false;
					}

					currentTask.setCompleted(true);
					successObj = dataStorage.saveIntoFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllDone = false;
					}
				}

				if (isMarkAllDone) {
					status = new Success(true, Message.SUCCESS_MARK_DONE);
				} else {
					status = new Success(false, Message.FAIL_MARK_DONE);
				}
			}

			redoStack.push(undoTask);

		} else {
			status = new Success(false, Message.FAIL_UNDO);
		}
		return status;
	}

	public Success redoTask() {

		Success status = null;
		FileIO dataStorage = new FileIO();

		if (redoStack.size() > 0) {
			TaskHistory redoTask = redoStack.pop();
			String undoOperation = redoTask.getOperation();
			Task taskObj = redoTask.getTask();

			// add the task obj.
			if (undoOperation.equalsIgnoreCase(KeywordConstant.KEYWORD_ADD)) {
				status = dataStorage.saveIntoFile(taskObj);
			}
			// delete the taskObj.
			else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DELETE)) {
				status = dataStorage.deleteFromFile(taskObj);
			}
			// revert the taskObj to previous version.
			else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UPDATE)) {
				Task taskToRevert = redoTask.getAuxTask();
				status = dataStorage.updateFromFile(taskObj, taskToRevert);
			} else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_UNDONE)) {

				boolean isMarkAllUndone = true;
				List<Task> taskListToRevert = redoTask.getTaskList();

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);

					Success successObj = dataStorage
							.deleteFromFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllUndone = false;
					}

					currentTask.setCompleted(false);
					successObj = dataStorage.saveIntoFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllUndone = false;
					}
				}

				if (isMarkAllUndone) {
					status = new Success(true, Message.SUCCESS_MARK_UNDONE);
				} else {
					status = new Success(false, Message.FAIL_MARK_UNDONE);
				}

			} else if (undoOperation
					.equalsIgnoreCase(KeywordConstant.KEYWORD_DONE)) {

				boolean isMarkAllDone = true;
				List<Task> taskListToRevert = redoTask.getTaskList();

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);

					Success successObj = dataStorage
							.deleteFromFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllDone = false;
					}

					currentTask.setCompleted(true);
					successObj = dataStorage.saveIntoFile(currentTask);

					if (!successObj.isSuccess()) {
						isMarkAllDone = false;
					}
				}

				if (isMarkAllDone) {
					status = new Success(true, Message.SUCCESS_MARK_DONE);
				} else {
					status = new Success(false, Message.FAIL_MARK_DONE);
				}
			}

			undoStack.push(redoTask);

		} else {
			status = new Success(false, Message.FAIL_REDO);
		}
		return status;

	}

}