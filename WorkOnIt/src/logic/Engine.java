package logic;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import resource.KeywordConstant;
import resource.Message;
import data.ReadFileIO;
import data.WriteFileIO;
import entity.SuccessDisplay;
import entity.Task;
import entity.Success;
import entity.TaskHistory;

public class Engine {

	private static final Logger LOGGER = Logger.getLogger(Engine.class
			.getName());

	/**
	 * This method will call the saveIntoFile method in fileIO to save the Task
	 *
	 * @param Task
	 *            Task which need to be save into the data file
	 * @return Success Success object return by the fileIO contain the success
	 *         Message.
	 */
	//@author A0119402R
	public Success addTask(Task task) {

		LOGGER.info("process adding of new task");

		Success status = null;

		assert (task != null);

		WriteFileIO dataStorage = new WriteFileIO();
		status = dataStorage.saveIntoFile(task);

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_ADD, task);

		UndoRedoManager.getInstance();
		UndoRedoManager.addUndoStack(taskHistoryObj);

		return status;
	}

	/**
	 * This method will call the loadFromFileTask method in fileIO to load the
	 * task based on the keyword entered
	 *
	 * @param String
	 *            Keyword which specific the task type
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	@SuppressWarnings("unchecked")
	public Success retrieveTask(String keyword) {

		LOGGER.info("process retrieve task keyword");

		Success status;

		assert (keyword != null);

		try {
			List<Task> taskList = new ArrayList<Task>();

			ReadFileIO dataStorage = new ReadFileIO();

			taskList.addAll((ArrayList<Task>) dataStorage.loadFromFileTask(
					keyword).getObj());

			status = new Success(taskList, true, Message.SUCCESS_RETRIEVE_LIST);

		} catch (Exception e) {
			status = new Success(false, e.getMessage());
		}

		return status;

	}

	/**
	 * This method will call the loadFromFileTask method in fileIO to load all
	 * Task.
	 *
	 * @param
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	@SuppressWarnings("unchecked")
	public Success retrieveTask() {

		LOGGER.info("process retrieve task");

		Success status;

		try {
			List<Task> taskList = new ArrayList<Task>();

			ReadFileIO dataStorage = new ReadFileIO();

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
	 * This method will call the loadFromStartDate method in fileIO to load the
	 * task based on the date entered
	 *
	 * @param Date
	 *            date which specific the task date to be retrieved
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	@SuppressWarnings("deprecation")
	public Success retrieveTask(Date date) throws IOException {

		LOGGER.info("process retrieve task singe date");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();
		date.setSeconds(0);
		status = dataStorage.loadFromStartDate(date);

		return status;
	}

	/**
	 * This method will call the loadFromBetweenDate method in fileIO to load
	 * the task between start date and end date entered
	 *
	 * @param Date
	 *            start date which specific the task start date.
	 * @param Date
	 *            end date which specific the task end date.
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	@SuppressWarnings("deprecation")
	public Success retrieveTask(Date startDate, Date endDate)
			throws IOException {

		LOGGER.info("process retrieve task within date range");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();
		startDate.setSeconds(0);
		status = dataStorage.loadFromBetweenDate(startDate, endDate);

		return status;
	}

	/**
	 * This method will call the loadFromPriority method in fileIO to load the
	 * task base on the priority
	 *
	 * @param int priority which specific the task priority to be retrieved
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success retrieveTask(int priority) throws IOException {

		LOGGER.info("process retrieve task with priority");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();

		assert (priority >= 0 && priority <= 2);

		status = dataStorage.loadFromPriority(priority);

		return status;
	}

	/**
	 * This method will call the loadFromPriorityAndDate method in fileIO to
	 * load the task based on the date and priority entered
	 *
	 * @param Date
	 *            date which specific the task date to be retrieved
	 * @param int priority which specific the task priority to be retrieved
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success retrieveTask(int priority, Date date) throws IOException {

		LOGGER.info("process retrieve task with priority on a single date");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();

		assert (date != null);
		assert (priority >= 0 && priority <= 2);

		status = dataStorage.loadFromPriorityAndDate(priority, date);

		return status;
	}

	/**
	 * This method will call the loadFromPriorityBetweenDate method in fileIO to
	 * load the task between start date and end date and priority entered
	 *
	 * @param Date
	 *            start date which specific the task start date.
	 * @param Date
	 *            end date which specific the task end date.
	 * @param int priority which specific the task priority to be retrieved
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success retrieveTask(int priority, Date startDate, Date endDate)
			throws IOException {

		LOGGER.info("process retrieve task with priority within a date range");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();

		assert (priority >= 0 && priority <= 2);
		assert (startDate != null && endDate != null);

		status = dataStorage.loadFromPriorityBetweenDate(priority, startDate,
				endDate);

		return status;
	}

	/**
	 * This method will call the loadCompletedTaskWithDate method in fileIO to
	 * load the task which has mark as completed with date
	 *
	 * @param boolean boolean which specific the whether the task has been
	 *        completed or not
	 * @param Date
	 *            date which specific the task date.
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success getCompleteTaskWithDate(boolean isComplete, Date date) {

		LOGGER.info("process retrieve completed/incomplete task on a given date");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();

		assert (date != null);

		status = dataStorage.loadCompletedTaskWithDate(isComplete, date);

		return status;
	}

	/**
	 * This method will call the loadCompletedTaskBetweenDate method in fileIO
	 * to load the task between start date and end date entered and which mark
	 * as completed
	 *
	 * @param boolean boolean which specific the whether the task has been
	 *        completed or not
	 * @param Date
	 *            start date which specific the task start date.
	 * @param Date
	 *            end date which specific the task end date.
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success getCompleteTaskBetweenDate(boolean isComplete,
			Date startDate, Date endDate) {

		LOGGER.info("process retrieve completed/incomplete task within date range");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();

		assert (startDate != null && endDate != null);

		status = dataStorage.loadCompletedTaskBetweenDate(isComplete,
				startDate, endDate);

		return status;
	}

	/**
	 * This method will call the searchFromFile method in fileIO to load the
	 * task base on the keyword entered.
	 *
	 * @param String
	 *            keyowrd which specific inside the task description
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success searchTask(String keyword) {

		LOGGER.info("process search task with keyword");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();

		assert (keyword != null);

		status = dataStorage.searchFromFile(keyword);

		return status;
	}

	/**
	 * This method will call the searchFromFileWithDate method in fileIO to load
	 * the task base on the keyword and date entered.
	 *
	 * @param String
	 *            keyowrd which specific inside the task description
	 * @param Date
	 *            date which specific the task date.
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success searchTask(String keyword, Date date) {

		LOGGER.info("process search task with keyword on a given date");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();

		assert (keyword != null && date != null);

		status = dataStorage.searchFromFileWithDate(keyword, date);

		return status;
	}

	/**
	 * This method will call the searchFromFileBetweenDate method in fileIO to
	 * load the task base on the keyword and between the start date and end date
	 * entered.
	 *
	 * @param String
	 *            keyowrd which specific inside the task description
	 * @param Date
	 *            start date which specific the task start date.
	 * @param Date
	 *            end date which specific the task end date.
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */
	@SuppressWarnings("deprecation")
	public Success searchTask(String keyword, Date startDate, Date endDate) {

		LOGGER.info("process search task with keyword within a date range");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();

		assert (keyword != null && startDate != null && endDate != null);

		startDate.setSeconds(0);
		status = dataStorage.searchFromFileBetweenDate(keyword, startDate,
				endDate);

		return status;
	}

	/**
	 * This method will call the deleteFromFile method in fileIO to delete the
	 * specific task parse in.
	 *
	 * @param List
	 *            List of task that need to be deleted from the data file
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success deleteTask(List<Task> deleteList) {

		LOGGER.info("process delete task");

		Success status = null;
		WriteFileIO dataStorage = new WriteFileIO();
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

		UndoRedoManager.getInstance();
		UndoRedoManager.addUndoStack(taskHistoryObj);

		if (isMarkAllDeleted) {
			status = new Success(true, Message.SUCCESS_DELETE);
		} else {
			status = new Success(false, Message.ERROR_DELETE);
		}

		return status;
	}

	/**
	 * This method will call the updateFromFile method in fileIO to update the
	 * specific task parse in
	 *
	 * @param Task
	 *            the new task that need to be updated
	 * @param Task
	 *            the old task that need to be overwrite.
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success updateTask(Task taskUpdate, Task taskOld) {

		LOGGER.info("process update task");

		Success status = null;
		WriteFileIO dataStorage = new WriteFileIO();

		if (taskOld.isCompleted()) {
			taskUpdate.setCompleted(true);
		}

		status = dataStorage.updateFromFile(taskUpdate, taskOld);

		TaskHistory taskHistoryObj = new TaskHistory(
				KeywordConstant.KEYWORD_UPDATE, taskUpdate, taskOld);
		UndoRedoManager.getInstance();
		UndoRedoManager.addUndoStack(taskHistoryObj);

		return status;
	}

	/**
	 * This method will call the getCompletedTask method in fileIO to load the
	 * task which mark as completed
	 *
	 * @param boolean boolean which specific the wether the task has been
	 *        completed or not
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success getCompleteTask(boolean isComplete) {

		LOGGER.info("process retrieve completed/incomplete task");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();

		status = dataStorage.getCompletedTask(isComplete);

		return status;
	}

	/**
	 * call the undoTaskFunction method in Utility class to undo the last task
	 *
	 * @param
	 * @return Success Success object which contain the message from utility
	 */
	//@author A0112694E
	public Success undoTask() {

		LOGGER.info("process undo");

		UndoRedoManager.getInstance();
		Success successObj = UndoRedoManager.undoTaskFunction();

		return successObj;

	}

	/**
	 * call the redoTaskFunction method in Utility class to redo the last task
	 *
	 * @param
	 * @return Success Success object which contain the message from utility
	 */
	public Success redoTask() {

		LOGGER.info("process redo");

		UndoRedoManager.getInstance();
		Success successObj = UndoRedoManager.redoTaskFunction();

		return successObj;
	}

	/**
	 * This method will retrieve Task(s) for displaying agenda view.
	 * 
	 * @param startDate
	 *            the beginning date
	 * @param endDate
	 *            the ending date
	 * @param displayType
	 *            the agenda view type
	 * @return
	 * @throws IOException
	 *             Will throw this exception if the file cannot be read or write
	 */
	//@author A0111916M
	@SuppressWarnings("unchecked")
	public SuccessDisplay retrieveDisplay(Date startDate, Date endDate,
			String displayType) throws IOException {

		LOGGER.info("process retrieve completed/incomplete task within date range");

		SuccessDisplay statusDisp = null;

		Success status = retrieveTask(startDate, endDate);

		if (status.isSuccess()) {

			List<Task> taskList = (ArrayList<Task>) status.getObj();

			status = retrieveTask(KeywordConstant.KEYWORD_FLOATING_TASK);

			if (status.isSuccess()) {

				statusDisp = appendFloatingTaskToRetrieve(displayType, status,
						taskList, startDate, endDate);
			} else {
				statusDisp = new SuccessDisplay(false, Message.ERROR_RETRIEVE);
			}

		} else {
			statusDisp = new SuccessDisplay(false, Message.ERROR_RETRIEVE);
		}

		return statusDisp;
	}

	/**
	 * Append the Floating Task List onto other Task List, for return to UI
	 * tier.
	 * 
	 * @param displayType
	 *            the agenda view type
	 * @param status
	 *            the Success object that retrieve Floating Task List
	 * @param taskList
	 *            the taskList that is other Task List
	 * @return
	 */

	@SuppressWarnings("unchecked")
	private SuccessDisplay appendFloatingTaskToRetrieve(String displayType,
			Success status, List<Task> taskList, Date startDate, Date endDate) {

		SuccessDisplay statusDisp = null;
		List<Task> floatingTaskList = (ArrayList<Task>) status.getObj();

		floatingTaskList = excludeCompletedTaskForDateView(displayType,
				floatingTaskList, startDate, endDate);

		taskList.addAll(floatingTaskList);

		statusDisp = new SuccessDisplay(displayType, taskList, true,
				Message.SUCCESS_RETRIEVE_LIST);
		return statusDisp;
	}

	/**
	 * This method will exclude completed floating task(s) if it is a daily
	 * agenda view
	 * 
	 * @param displayType
	 *            the agenda view type
	 * @param floatingTaskList
	 *            list of unprocessed floating task
	 * @return List<Task> The list of excluded floating task, if needed.
	 */

	private List<Task> excludeCompletedTaskForDateView(String displayType,
			List<Task> floatingTaskList, Date startDate, Date endDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Task> incompleteFloatingTaskList = new ArrayList<Task>();

		for (int i = 0; i < floatingTaskList.size(); i++) {

			Task currTask = floatingTaskList.get(i);

			String startDateString = sdf.format(startDate);
			String taskDateString = sdf.format(currTask.getDateCreated());

			if (!(currTask.isCompleted()
					&& (displayType.equals(KeywordConstant.KEYWORD_DAY) || displayType
							.equals(KeywordConstant.KEYWORD_DATE)) && !startDateString
						.equals(taskDateString))) {
				incompleteFloatingTaskList.add(currTask);
			}
		}

		return incompleteFloatingTaskList;
	}

	/**
	 * This method will mark the list of task as done
	 * 
	 * @param List
	 *            the list of task that need to be mark as done
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success markAsDone(List<Task> doneList) {

		LOGGER.info("process mark task as done");

		Success status = null;
		WriteFileIO dataStorage = new WriteFileIO();
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
		UndoRedoManager.getInstance();
		UndoRedoManager.addUndoStack(taskHistoryObj);

		if (isMarkAllDone) {
			status = new Success(true, Message.SUCCESS_MARK_DONE);
		} else {
			status = new Success(false, Message.FAIL_MARK_DONE);
		}

		return status;
	}

	/**
	 * This method will mark the list of task as undone
	 * 
	 * @param List
	 *            the list of task that need to be mark as undone
	 * @return Success Success object return by the fileIO contain the success
	 *         Message and Array List of task retrieve from data file.
	 */

	public Success markAsUndone(List<Task> undoneList) {

		LOGGER.info("process mark task as undone");

		Success status = null;
		WriteFileIO dataStorage = new WriteFileIO();
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
		UndoRedoManager.getInstance();
		UndoRedoManager.addUndoStack(taskHistoryObj);

		if (isMarkAllUndone) {
			status = new Success(true, Message.SUCCESS_MARK_UNDONE);
		} else {
			status = new Success(false, Message.FAIL_MARK_UNDONE);
		}

		return status;
	}

	/**
	 * This method will get the list of History that were saved.
	 * 
	 * @return Success object
	 */
	public Success getHistory() {

		LOGGER.info("process get history from file");

		Success status = null;
		ReadFileIO dataStorage = new ReadFileIO();

		status = dataStorage.getHistory();

		return status;
	}

}