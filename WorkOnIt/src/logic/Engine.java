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
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
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
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	// retrieve all task from database
	@SuppressWarnings("unchecked")
	public Success retrieveTask(String keyword) {

		Success status;

		try {
			List<Task> taskList = new ArrayList<Task>();

			FileIO dataStorage = new FileIO();

			taskList.addAll((ArrayList<Task>) dataStorage.loadFromFileTask(
					keyword).getObj());

			status = new Success(taskList, true, Message.SUCCESS_RETRIEVE_LIST);

		} catch (Exception e) {
			status = new Success(false, e.getMessage());
		}

		return status;

	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	// retrieve task based on keyword
	@SuppressWarnings("unchecked")
	public Success retrieveTask() {

		Success status;

		try {
			List<Task> taskList = new ArrayList<Task>();

			FileIO dataStorage = new FileIO();

			taskList.addAll((ArrayList<Task>) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_FLOATING_TASK).getObj());
			taskList.addAll((ArrayList<Task>) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_NORMAL_TASK).getObj());
			taskList.addAll((ArrayList<Task>) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_DEADLINE_TASK).getObj());
			taskList.addAll((ArrayList<Task>) dataStorage.loadFromFileTask(
					KeywordConstant.KEYWORD_RECUR_TASK).getObj());

			status = new Success(taskList, true, Message.SUCCESS_RETRIEVE_LIST);

		} catch (Exception e) {
			status = new Success(false, e.getMessage());
		}

		return status;

	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	// retrieve selected task entity from database.
	public Success retrieveTask(Task task) throws IOException {

		Success status = null;
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

		status = dataStorage.loadFromFileTask(file_type);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	// retrieve task with specific START DATE
	// Affected File > NormalTask, DeadlineTask, RecurTask
	public Success retrieveTask(Date date) throws IOException {

		Success status = null;
		// i check the date.
		FileIO dataStorage = new FileIO();

		status = dataStorage.loadFromStartDate(date);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	// retrieve task with specific INBETWEEN DATE
	// Affected File > NormalTask, DeadlineTask, RecurTask
	public Success retrieveTask(Date startDate, Date endDate)
			throws IOException {

		Success status = null;

		FileIO dataStorage = new FileIO();

		status = dataStorage.loadFromBetweenDate(startDate, endDate);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	// retrieve task with specific priority (EG. URGENT)
	// Affected File > NormalTask, DeadlineTask, RecurTask
	public Success retrieveTask(int priority) throws IOException {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.loadFromPriority(priority);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Success retrieveTask(int priority, Date date) throws IOException {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.loadFromPriorityAndDate(priority, date);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Success retrieveTask(int priority, Date startDate, Date endDate)
			throws IOException {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.loadFromPriorityBetweenDate(priority, startDate,
				endDate);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	// retrieve task with specific task and date (EG. URGENT)
	// Affected File > NormalTask, DeadlineTask, RecurTask
	public Success retrieveTask(Task task, Date date) throws IOException {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.loadFromStartDateWithTask(task, date);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	// retrieve task with specific priority (EG. URGENT)
	// Affected File > NormalTask, DeadlineTask, RecurTask
	public Success retrieveTask(Task task, Date startDate, Date endDate)
			throws IOException {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.loadFromBetweenDateWithTask(task, startDate,
				endDate);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	@SuppressWarnings("unchecked")
	public SuccessDisplay retrieveDisplay(Date startDate, Date endDate,
			String displayType) throws IOException {

		SuccessDisplay statusDisp = null;

		Success status = retrieveTask(startDate, endDate);

		if (status.isSuccess()) {

			List<Task> taskList = (ArrayList<Task>) status.getObj();

			for (int i = 0; i < taskList.size(); i++) {
				String name = taskList.get(i).getTaskName();
				System.out.println(name);
			}

			status = retrieveTask(KeywordConstant.KEYWORD_FLOATING_TASK);

			if (status.isSuccess()) {
				List<Task> floatingTaskList = (ArrayList<Task>) status.getObj();
				taskList.addAll(floatingTaskList);

				statusDisp = new SuccessDisplay(displayType, taskList, true,
						Message.SUCCESS_RETRIEVE_LIST);
			} else {
				statusDisp = new SuccessDisplay(false, Message.ERROR_RETRIEVE);
			}

		} else {
			statusDisp = new SuccessDisplay(false, Message.ERROR_RETRIEVE);
		}

		return statusDisp;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Success searchTask(String keyword) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.searchFromFile(keyword);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Success searchTask(String keyword, Date date) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.searchFromFileWithDate(keyword, date);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Success searchTask(String keyword, Date startDate, Date endDate) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.searchFromFileBetweenDate(keyword, startDate,
				endDate);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	// delete task with specific ID
	public Success deleteTask(List<Task> deleteList) {

		Success status = null;
		FileIO dataStorage = new FileIO();
		boolean isMarkAllDeleted = true;

		for (int i = 0; i < deleteList.size(); i++) {

			Task currentTask = deleteList.get(i);
			Success successObj = dataStorage.deleteFromFile(currentTask);

			if (!successObj.isSuccess()) {
				isMarkAllDeleted = false;
			}
		}

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_DELETE, deleteList);
		undoStack.push(taskHistoryObj);

		if (isMarkAllDeleted) {
			status = new Success(true, Message.SUCCESS_DELETE);
		} else {
			status = new Success(false, Message.ERROR_DELETE);
		}

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Success updateTask(Task taskUpdate, Task taskOld) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		if (taskOld.isCompleted()) {
			taskUpdate.setCompleted(true);
		}

		status = dataStorage.updateFromFile(taskUpdate, taskOld);

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_UPDATE, taskUpdate, taskOld);
		undoStack.push(taskHistoryObj);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Success getCompleteTask(boolean isComplete) {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.getCompletedTask(isComplete);

		return status;
	}
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
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
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
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
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
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
				List<Task> taskListToRevert = undoTask.getTaskList();
				boolean isMarkAllDeleted = true;

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);
					Success successObj = dataStorage.saveIntoFile(currentTask);

					if (successObj == null || !successObj.isSuccess()) {
						isMarkAllDeleted = false;
					}
				}

				if (isMarkAllDeleted) {
					status = new Success(true, Message.SUCCESS_DELETE);
				} else {
					status = new Success(false, Message.ERROR_DELETE);
				}
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
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
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
				List<Task> taskListToRevert = redoTask.getTaskList();
				boolean isMarkAllDeleted = true;

				for (int i = 0; i < taskListToRevert.size(); i++) {

					Task currentTask = taskListToRevert.get(i);
					Success successObj = status = dataStorage
							.deleteFromFile(currentTask);

					if (successObj == null || !successObj.isSuccess()) {
						isMarkAllDeleted = false;
					}
				}

				if (isMarkAllDeleted) {
					status = new Success(true, Message.SUCCESS_DELETE);
				} else {
					status = new Success(false, Message.ERROR_DELETE);
				}
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
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public Success getHistory() {

		Success status = null;
		FileIO dataStorage = new FileIO();

		status = dataStorage.getHistory();

		return status;
	}

}